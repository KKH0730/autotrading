package st.seno.autotrading.data.network.repository

import st.seno.autotrading.data.network.response_model.ServiceResponse
import st.seno.autotrading.data.network.response_model.TradingData

interface AutoTradingRepository  {

    suspend fun startAutoTrading(
        userKey: String,
        marketId: String,
        quantityRatio: Int,
        stopLoss: Int,
        takeProfit: Int,
        correctionValue: Float,
        startDate: Long,
        endDateTime: Long,
        tradingStrategy: String
    ): ServiceResponse

    suspend fun stopAutoTrading(userKey: String): ServiceResponse

    suspend fun checkAutoTrading(userKey: String): ServiceResponse

    suspend fun getTradingHistory(userKey: String): List<TradingData>
}