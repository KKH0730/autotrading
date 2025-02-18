package st.seno.autotrading.data.network.repository

import kotlinx.coroutines.flow.Flow
import st.seno.autotrading.data.network.response_model.TradingData

interface TradingDataRepository {
    suspend fun reqTradingData(startDate: String): Flow<List<TradingData>>
}