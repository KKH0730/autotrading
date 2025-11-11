package st.seno.autotrading.server.core.data.network.model


data class TradingData(
    val uuid: String,
    val bidUuid: String,
    val quantityRatio: Int,
    val tradingStrategy: String,
    val stopLoss: Int,
    val takeProfit: Int,
    val correctionValue: Float,
    val endDateTime: String,
    val order: IndividualOrder
)