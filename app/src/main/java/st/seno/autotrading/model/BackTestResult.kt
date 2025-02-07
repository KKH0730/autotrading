package st.seno.autotrading.model

data class BackTestResult(
    val totalProfit: Double,
    val tradeCount: Int,
    val totalFee: Double,
    val totalReturnRate: Double,
    val winRate: Double,
    val maximumProfitRate: Double,
    val maximumLossRate: Double,
    var trigger: Boolean
)