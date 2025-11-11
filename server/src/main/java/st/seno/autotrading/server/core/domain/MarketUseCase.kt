package st.seno.autotrading.server.core.domain

import org.springframework.stereotype.Service
import st.seno.autotrading.server.core.data.network.model.Crypto
import st.seno.autotrading.server.core.data.network.repository.MarketRepository
import st.seno.autotrading.server.core.extension.safeCall
import st.seno.autotrading.server.core.data.network.model.Result

@Service
class MarketUseCase(
    private val marketRepository: MarketRepository
){
    suspend fun reqMarketCrypto(): Result<List<Crypto>>  = safeCall {
        marketRepository.reqMarketCrypto()
    }
}

