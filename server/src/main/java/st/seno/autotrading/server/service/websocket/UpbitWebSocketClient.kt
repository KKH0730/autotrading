package st.seno.autotrading.server.service.websocket

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.springframework.stereotype.Component
import st.seno.autotrading.server.config.UpbitProperties
import st.seno.autotrading.server.config.UpbitWebSocketStarter
import st.seno.autotrading.server.core.data.network.model.PingResponse
import st.seno.autotrading.server.core.data.network.model.Ticker
import st.seno.autotrading.server.core.extension.parseOrNull
import java.lang.Exception
import java.util.concurrent.TimeUnit

@Component
class UpbitWebSocketClient(
    private val upbitProperties: UpbitProperties,
    private val autoTradingHandler: AutoTradingSocketHandler
) {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .pingInterval(10, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private var webSocket: WebSocket? = null
    private var cryptos: List<String> = listOf()

    /**
     * @param cryptos KRW-BTC, KRW-ETH, KRW-XRP
     */
    suspend fun connect(
        cryptos: List<String>,
        maxRetries: Int = 5,
        initialDelayMs: Long = 1000
    ) {
        this@UpbitWebSocketClient.cryptos = cryptos

        var retryCount = 0
        var currentDelay = initialDelayMs

        withContext(Dispatchers.IO) {
            launch {
                fun attemptConnection() {
                    val request = Request.Builder()
                        .url("wss://api.upbit.com/websocket/v1")
                        .build()

                    try {
                        val upbitClient = client.newBuilder()
                            .pingInterval(10, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .addInterceptor { chain ->
                                val chainRequest = chain.request()
                                val response = chain.proceed(chainRequest)
                                response
                            }
                            .build()

                        webSocket = upbitClient.newWebSocket(request, object : WebSocketListener() {

                            override fun onOpen(webSocket: WebSocket, response: Response) {
                                println("✅ [Upbit WebSocket] 연결 성공")
                                sendTickerSubscribe(cryptos)
                            }

                            override fun onMessage(webSocket: WebSocket, text: String) {
                                super.onMessage(webSocket, text)
                                val pingResponse = text.parseOrNull<PingResponse>()
                                if (pingResponse?.status != "UP") {
                                    text.parseOrNull<Ticker>()?.run {
                                        UpbitWebSocketStarter.Companion.tickersMap[this.code] = this
                                        autoTradingHandler.sendMessageToAllClient(
                                            mapOf(
                                                "mode" to "upbit",
                                                "data" to this.toMapSerializedName()
                                            )
                                        )
                                    }
                                }
                            }

                            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                                val text = bytes.utf8()
                                val pingResponse = text.parseOrNull<PingResponse>()
                                if (pingResponse?.status != "UP") {
                                    text.parseOrNull<Ticker>()?.run {
                                        UpbitWebSocketStarter.Companion.tickersMap[this.code] = this
                                        autoTradingHandler.sendMessageToAllClient(
                                            mapOf(
                                                "mode" to "upbit",
                                                "data" to this.toMapSerializedName()
                                            )
                                        )
                                    }
                                }
                            }

                            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                                println("⚠️ [Upbit WebSocket] 닫는 중: $reason")
                                handleReconnect("onClosing: $reason")
                            }

                            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                                super.onClosed(webSocket, code, reason)
                                println("⚠️ [Upbit WebSocket] 닫힘: $reason")
                                handleReconnect("Closed: $reason")
                            }

                            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                                println("❌ [Upbit WebSocket] 실패: ${t.message}")
                                handleReconnect("Failure: ${t.message}")
                            }

                            fun handleReconnect(reason: String) {
                                if (retryCount >= maxRetries) {
                                    println("❌ 재연결 ${maxRetries}회 실패 — 중단")
                                    return
                                }

                                retryCount++
                                println("⚠️ 연결 끊김 ($reason). ${currentDelay}ms 후 재시도 (${retryCount}/$maxRetries)")

                                Thread.sleep(currentDelay)
                                currentDelay = (currentDelay * 2).coerceAtMost(30_000)

                                attemptConnection()
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()

                    }
                }

                attemptConnection()
            }

            launch {
                while (webSocket != null) {
                    ping()
                    delay(120000)
                }
            }
        }
    }

    private fun sendTickerSubscribe(markets: List<String>) {
        val codes = markets.joinToString(",", "[", "]") { "\"$it\"" }

        val message = """
            [
              {"ticket":"server"},
              {"type":"ticker","codes":$codes,"is_only_realtime":true},
              {"format":"SIMPLE"}
            ]
        """.trimIndent()

        webSocket?.send(message)
        println("📡 구독요청 전송: $message")
    }

    fun ping() {
        val message = "PING"
        webSocket?.send(message)
    }
}