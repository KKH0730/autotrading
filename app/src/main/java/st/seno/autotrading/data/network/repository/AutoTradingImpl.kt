package st.seno.autotrading.data.network.repository

import st.seno.autotrading.data.mapper.TradingDataMapper
import st.seno.autotrading.data.network.response_model.ServiceResponse
import st.seno.autotrading.data.network.response_model.TradingData
import st.seno.autotrading.data.network.service.AutoTradingService
import javax.inject.Inject

class AutoTradingImpl @Inject constructor(
    private val autoTradingService: AutoTradingService,
    private val mapper: TradingDataMapper
) : AutoTradingRepository {

    override suspend fun startAutoTrading(
        userKey: String,
        marketId: String,
        quantityRatio: Int,
        stopLoss: Int,
        takeProfit: Int,
        correctionValue: Float,
        startDate: Long,
        endDateTime: Long,
        tradingStrategy: String
    ): ServiceResponse {
        val response = autoTradingService.startTrading(
            mapOf(
                "userKey" to userKey,
                "marketId" to marketId,
                "quantityRatio" to quantityRatio,
                "stopLoss" to stopLoss,
                "takeProfit" to takeProfit,
                "correctionValue" to correctionValue,
                "startDate" to startDate,
                "endDateTime" to endDateTime,
                "tradingStrategy" to tradingStrategy
            )
        )
        return response
    }

    override suspend fun stopAutoTrading(userKey: String): ServiceResponse = autoTradingService.stopAutoTrading(mapOf("userKey" to userKey))

    override suspend fun checkAutoTrading(userKey: String): ServiceResponse = autoTradingService.checkAutoTrading(mapOf("userKey" to userKey))

    override suspend fun getTradingHistory(userKey: String): List<TradingData> = mapper.fromRemote(model = autoTradingService.getTradingHistory(mapOf("userKey" to userKey)))
}