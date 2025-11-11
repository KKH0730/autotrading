package st.seno.autotrading.server.service.websocket

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler
import st.seno.autotrading.server.controller.AutoTradingController
import st.seno.autotrading.server.model.AutoTradingConstants
import st.seno.autotrading.server.service.rest_api.AutoTradingApiService
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val handler: MySocketHandler,
    private val autoTradingHandler: AutoTradingSocketHandler
) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(handler, "/ws").setAllowedOrigins("*")
        registry.addHandler(autoTradingHandler, "/ws/trading").setAllowedOrigins("*")
    }
}

@Component
class MySocketHandler : TextWebSocketHandler() {
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("📩 Received from client: ${message.payload}")
        session.sendMessage(TextMessage("서버 응답: ${message.payload}"))
    }
}

@Component
class AutoTradingSocketHandler(
    @Lazy private val controller: AutoTradingController
) : TextWebSocketHandler() {
    // 현재 연결된 세션을 저장
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()
    private val userKeyMap = mutableMapOf<String, String>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userKey = session.handshakeHeaders["userKey"]?.firstOrNull()
        userKey?.let {
            userKeyMap[userKey]?.let { sessionId -> sessions.remove(sessionId) }
            userKeyMap[userKey] = session.id

        }
        sessions[session.id] = session
        userKey?.let {
            controller.isRunningAutoTrading(body = mapOf("userKey" to userKey))
            controller.getTradingHistory(body = mapOf("userKey" to userKey))
        }
        println("새 클라이언트 연결: ${session.id}, 총 연결: ${sessions.size}, userKey : $userKey")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("📩 Received from ${session.id}: ${message.payload}")

        // 1) 특정 클라이언트에게 응답
        session.sendMessage(TextMessage("서버 응답: ${message.payload}"))

        // 2) 모든 클라이언트에게 브로드캐스트
        for (s in sessions.values) {
            if (s.isOpen) {
                s.sendMessage(TextMessage("[Broadcast] ${message.payload}"))
            }
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session.id)
        println("클라이언트 연결 종료: ${session.id}, 총 연결: ${sessions.size}")
    }

    fun sendMessageToClient(userKey: String, map: Map<String, Any?>) {
        val mapper = jacksonObjectMapper().apply { registerKotlinModule() }

        userKeyMap[userKey]
            ?.let { sessionId -> sessions[sessionId] }
            ?.takeIf { it.isOpen }
            ?.run {
                sendMessage(TextMessage(mapper.writeValueAsString(map)))
            }
    }
}