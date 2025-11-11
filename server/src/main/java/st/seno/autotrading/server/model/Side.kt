package st.seno.autotrading.server.model

enum class Side(val value: String) {
    BID(value = "bid"), // 매수
    ASK(value = "ask") // 매도
}