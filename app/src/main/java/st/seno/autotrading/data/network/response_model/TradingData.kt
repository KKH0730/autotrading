package st.seno.autotrading.data.network.response_model

data class TradingData(
    val uuid: String,
    val quantityRatio: Int,
    val tradingStrategy: String,
    val stopLoss: Int,
    val takeProfit: Int,
    val correctionValue: Float,
    val endDateTime: String,
    val order: IndividualOrder
)