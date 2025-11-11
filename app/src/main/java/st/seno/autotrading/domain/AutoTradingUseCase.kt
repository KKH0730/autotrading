package st.seno.autotrading.domain

import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.repository.AutoTradingRepository
import st.seno.autotrading.data.network.response_model.ServiceResponse
import st.seno.autotrading.data.network.response_model.TradingData
import st.seno.autotrading.extensions.safeCall
import javax.inject.Inject

class AutoTradingUseCase @Inject constructor(
    private val autoTradingRepository: AutoTradingRepository
) {
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
    ): Result<ServiceResponse> = safeCall {
        autoTradingRepository.startAutoTrading(
            userKey = userKey,
            marketId = marketId,
            quantityRatio = quantityRatio,
            stopLoss = stopLoss,
            takeProfit = takeProfit,
            correctionValue = correctionValue,
            startDate = startDate,
            endDateTime = endDateTime,
            tradingStrategy = tradingStrategy
        )
    }

    suspend fun stopAutoTrading(userKey: String): Result<ServiceResponse> = safeCall { autoTradingRepository.stopAutoTrading(userKey = userKey) }

    suspend fun checkAutoTrading(userKey: String): Result<ServiceResponse> = safeCall { autoTradingRepository.checkAutoTrading(userKey = userKey) }

    suspend fun getTradingHistory(userKey: String): Result<List<TradingData>> = safeCall { autoTradingRepository.getTradingHistory(userKey = userKey) }
}