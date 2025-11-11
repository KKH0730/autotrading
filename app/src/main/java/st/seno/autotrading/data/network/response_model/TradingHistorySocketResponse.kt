package st.seno.autotrading.data.network.response_model

data class TradingHistorySocketResponse(
    val mode: String,
    val data: List<TradingData>?
)