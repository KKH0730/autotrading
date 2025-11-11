package st.seno.autotrading.server.model

data class TradingOptions(
    val marketId: String,
    val quantityRatio: Int,
    val stopLoss: Int,
    var stopLossPrice: String = "매수체결 후에 결정됩니다.",
    val takeProfit: Int,
    var takeProfitPrice: String = "매수체결 후에 결정됩니다.",
    val correctionValue: Float,
    val startDate: Long,
    val endDateTime: Long,
    val tradingStrategy: String
)