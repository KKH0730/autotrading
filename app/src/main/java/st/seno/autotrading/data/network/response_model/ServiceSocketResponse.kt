package st.seno.autotrading.data.network.response_model

import st.seno.autotrading.data.network.model.TradingOptions

data class ServiceSocketResponse(
    val mode: String,
    val data: OperationData
)

data class OperationData(
    val isRunning: Boolean,
    val message: String,
    val tradingOptions: TradingOptions?
)