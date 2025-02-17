package st.seno.autotrading.data.network.socket

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import st.seno.autotrading.data.network.ACCESS_KEY
import st.seno.autotrading.data.network.SECRET_KEY
import st.seno.autotrading.data.network.model.Ticker
import java.util.UUID
import java.util.concurrent.TimeUnit

const val NORMAL_CLOSURE_STATUS = 1000

class RxSocketClient {
    private val client: OkHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null
    val isConnected: Boolean get() = webSocket != null

    fun connect(): Flow<SockResponse> {
        return channelFlow {
            val url = "wss://api.upbit.com/websocket/v1"

            val algorithm: Algorithm = Algorithm.HMAC256(SECRET_KEY)

            val jwtToken: String = JWT.create()
                .withClaim("access_key", ACCESS_KEY)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm)

            val authenticationToken = "Bearer $jwtToken"

            val request = Request.Builder()
                .addHeader("authorization", authenticationToken)
                .url(url)
                .build()

            try {
                client.newBuilder()
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

                client.newWebSocket(request, object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        super.onOpen(webSocket, response)
                        this@RxSocketClient.webSocket = webSocket
                        trySend(SockResponse.Open(webSocket = webSocket, response = response))
                    }

                    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                        super.onMessage(webSocket, bytes)
                        val message = bytes.utf8()
                        trySend(SockResponse.Message(data = message))
                    }

                    override fun onMessage(webSocket: WebSocket, text: String) {
                        trySend(SockResponse.Message(data = text))
                        super.onMessage(webSocket, text)
                    }

                    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                        trySend(SockResponse.Closing(webSocket = webSocket, code = code, reason = reason))
                        super.onClosing(webSocket, code, reason)
                        disconnect()
                    }

                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                        trySend(SockResponse.Closed(webSocket = webSocket, code = code, reason = reason))
                        super.onClosed(webSocket, code, reason)
                        disconnect()
                    }

                    override fun onFailure(
                        webSocket: WebSocket,
                        t: Throwable,
                        response: Response?,
                    ) {
                        trySend(SockResponse.Failure(t = t))
                        super.onFailure(webSocket, t, response)
                        disconnect()
                    }
                })
            } catch (e: Exception) {
                trySend(SockResponse.Failure(t = e))
            }

            awaitClose {
                webSocket?.close(1000, null)
            }
        }
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

    fun disconnect() {
        webSocket?.run {
            close(NORMAL_CLOSURE_STATUS, null)
            cancel()
        }
        webSocket = null
    }

    fun release() {
        webSocket?.run {
            close(NORMAL_CLOSURE_STATUS, null)
            cancel()
        }
        client.dispatcher.executorService.shutdown()
    }

    companion object {
        const val MAIN_SOCKET = "main_socket"
        const val TRADING_VIEW_CANDLE_SOCKET = "tradingViewCandleSocket"
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
