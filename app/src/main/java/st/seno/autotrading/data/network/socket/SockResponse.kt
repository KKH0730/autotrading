package st.seno.autotrading.data.network.socket

import okhttp3.Response
import okhttp3.WebSocket

sealed class SockResponse {
    data class Message(val data: String) : SockResponse()
    data class Open(val webSocket: WebSocket, val response: Response) : SockResponse()
    data class Closing(val webSocket: WebSocket, val code: Int, val reason: String) : SockResponse()
    data class Closed(val webSocket: WebSocket, val code: Int, val reason: String) : SockResponse()
    data class Failure(val t: Throwable) : SockResponse()
    data class Reconnect(val message: String) : SockResponse()
    data class TerminationState(val message: String) : SockResponse()
}