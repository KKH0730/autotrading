package st.seno.autotrading.domain

import st.seno.autotrading.data.network.model.Crypto
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.repository.MarketRepository
import st.seno.autotrading.extensions.safeCall
import javax.inject.Inject

class MarketUseCase @Inject constructor(
    private val marketRepository: MarketRepository
){
    suspend fun reqMarketCrypto(): Result<List<Crypto>>  = safeCall {
        marketRepository.reqMarketCrypto()
    }
}

