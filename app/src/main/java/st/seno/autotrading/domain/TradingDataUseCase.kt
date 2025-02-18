package st.seno.autotrading.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import st.seno.autotrading.data.network.repository.TradingDataRepository
import st.seno.autotrading.data.network.response_model.TradingData
import st.seno.autotrading.di.Qualifiers
import javax.inject.Inject

class TradingDataUseCase @Inject constructor(
    private val tradingDataRepository: TradingDataRepository,
    @Qualifiers.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun reqTradingData(startDate: String): Flow<List<TradingData>>
    = tradingDataRepository.reqTradingData(startDate = startDate)
}