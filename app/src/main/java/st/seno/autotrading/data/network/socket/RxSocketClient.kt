package st.seno.autotrading.data.network.socket

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import st.seno.autotrading.extensions.parseOrNull
import st.seno.autotrading.model.PingResponse
import java.util.concurrent.TimeUnit

const val NORMAL_CLOSURE_STATUS = 1000

class RxSocketClient {
    private val client: OkHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null
    private var isFinishReconnect = false

    val isConnected get() = webSocket != null

    fun connect(
        url: String,
        authenticationToken: String = "",
        userKey: String = "",
        maxRetries: Int = 5,
        initialDelayMs: Long = 1000
    ): Flow<SockResponse> = channelFlow {
        isFinishReconnect = false

        var retryCount = 0
        var currentDelay = initialDelayMs

        fun attemptConnection() {
            val builder = Request.Builder().url(url)
            if (authenticationToken.isNotEmpty()) {
                builder.addHeader("authorization", authenticationToken)
            }

            if (userKey.isNotEmpty()) {
                builder.addHeader("userKey", userKey)

            }
            val request = builder.build()

            try {
                val localClient = client.newBuilder()
                    .pingInterval(10, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor { chain ->
                        chain.proceed(chain.request())
                    }
                    .build()

                webSocket = localClient.newWebSocket(request, object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        retryCount = 0
                        currentDelay = initialDelayMs
                        trySend(SockResponse.Open(webSocket, response))
                    }

                    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                        val message = bytes.utf8()
                        val pingResponse = message.parseOrNull<PingResponse>()
                        if (pingResponse?.status != "UP") {
                            trySend(SockResponse.Message(message))
                        }
                    }

                    override fun onMessage(webSocket: WebSocket, text: String) {
                        val pingResponse = text.parseOrNull<PingResponse>()
                        if (pingResponse?.status != "UP") {
                            trySend(SockResponse.Message(text))
                        }
                    }

                    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                        trySend(SockResponse.Closing(webSocket, code, reason))
                        handleReconnect("onClosing: $reason")
                    }

                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                        trySend(SockResponse.Closed(webSocket, code, reason))
                        handleReconnect("Closed: $reason")
                    }

                    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                        trySend(SockResponse.Failure(t))
                        handleReconnect("Failure: ${t.message}")
                    }

                    fun handleReconnect(reason: String) {
                        if (isFinishReconnect) {
                            retryCount = maxRetries
                            return
                        }

                        if (retryCount >= maxRetries) {
                            trySend(SockResponse.TerminationState("❌ 재연결 ${maxRetries}회 실패 — 중단"))
                            return
                        }

                        retryCount++
                        trySend(SockResponse.Reconnect("⚠️ 연결 끊김 ($reason). ${currentDelay}ms 후 재시도 (${retryCount}/$maxRetries)"))

                        launch {
                            delay(currentDelay)
                            currentDelay = (currentDelay * 2).coerceAtMost(30_000)
                            attemptConnection()
                        }
                    }
                })
            } catch (e: Exception) {
                trySend(SockResponse.Failure(e))
            }
        }

        attemptConnection()

        awaitClose {
            webSocket?.close(1000, "Client closed")
        }
    }

    fun ping() {
        val message = "PING"
        webSocket?.send(message)
    }

    fun sendMessageDaysTicker(
        cryptos: List<String>, // ex, KRW-BTC, KRW-ETH, KRW-XRP
        isRealTime: Boolean
    ) {
        val cryptoCodes = cryptos.joinToString(separator = ",", prefix = "[", postfix = "]") { "\"$it\"" }
        val message = """[
                {
                    "ticket": "ticker"
                },
                {
                    "type": "ticker",
                    "codes": $cryptoCodes,
                    "is_only_snapshot": ${!isRealTime},
                    "is_only_realtime": $isRealTime
                },
                { 
                    "format": "SIMPLE" 
                } 
            ]"""
        webSocket?.send(message)
    }

    fun sendMessageCandle(
        marketId: String, // ex, KRW-BTC, KRW-ETH, KRW-XRP
        type: String,// candles.1s: 초봉
        isOnlySnapshot: Boolean = false,
        isOnlyRealtime: Boolean = true
    ) {
        val message = """[
                {
                    "ticket": "candle"
                },
                {
                    "type": "$type",
                    "codes": ["$marketId"],
                    "is_only_snapshot": $isOnlySnapshot,
                    "is_only_realtime": $isOnlyRealtime
                },
                {
                    "format": "SIMPLE"
                }
            ]"""
        webSocket?.send(message)
    }

    fun release() {
        isFinishReconnect = true
        webSocket?.run {
            close(NORMAL_CLOSURE_STATUS, null)
            cancel()
        }
        webSocket = null
    }

    companion object {
        const val LOCAL_SOCKET = "local_socket"
        private var instances = HashMap<String, RxSocketClient?>()

        fun getInstance(key: String?): RxSocketClient {
            val instance = if (key == null) {
                RxSocketClient()
            } else {
                val rxSocketClient = instances[key]

                if (rxSocketClient != null) {
                    rxSocketClient
                } else {
                    val newInstance = RxSocketClient()
                    instances[key] = newInstance
                    newInstance
                }
            }

            return instance
        }

        fun releaseSocket(key: String) {
            instances[key]?.release()
            instances[key] = null
        }

        fun releaseAllSocket() {
            instances.forEach { it.value?.release() }
            instances.clear()
        }
    }
}
