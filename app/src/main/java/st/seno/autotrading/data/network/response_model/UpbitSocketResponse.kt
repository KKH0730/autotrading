package st.seno.autotrading.data.network.response_model

import st.seno.autotrading.data.network.model.Ticker

data class UpbitSocketResponse(
    val mode: String,
    val data: Ticker
)