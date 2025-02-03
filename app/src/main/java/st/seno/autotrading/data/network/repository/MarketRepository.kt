package st.seno.autotrading.data.network.repository

import kotlinx.coroutines.flow.Flow
import st.seno.autotrading.data.network.model.Crypto
import st.seno.autotrading.data.network.model.Result

interface MarketRepository {
    suspend fun reqMarketCrypto(): Flow<Result<List<Crypto>>>
}