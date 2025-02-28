package st.seno.autotrading.domain

import st.seno.autotrading.di.Qualifiers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import st.seno.autotrading.data.network.model.Crypto
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.repository.MarketRepository
import st.seno.autotrading.extensions.catchError
import javax.inject.Inject

class MarketUseCase @Inject constructor(
    private val marketRepository: MarketRepository,
    @Qualifiers.IoDispatcher private val ioDispatcher: CoroutineDispatcher
){
    suspend fun reqMarketCrypto(): Result<List<Crypto>> {
        return marketRepository.reqMarketCrypto().catchError(dispatcher = ioDispatcher)
    }
}

