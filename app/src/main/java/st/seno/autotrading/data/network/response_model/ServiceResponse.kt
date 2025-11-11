package st.seno.autotrading.data.network.response_model

import st.seno.autotrading.data.network.model.TradingOptions

data class ServiceResponse(
    val isRunning: Boolean,
    val message: String,
    val tradingOptions: TradingOptions?
)