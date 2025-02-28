package st.seno.autotrading.data.network.repository

import st.seno.autotrading.data.mapper.MarketCryptoMapper
import st.seno.autotrading.data.network.model.Crypto
import st.seno.autotrading.data.network.service.MarketService
import javax.inject.Inject

class MarketImpl @Inject constructor(
    private val service: MarketService,
    private val mapper: MarketCryptoMapper

) : MarketRepository {
    override suspend fun reqMarketCrypto(): List<Crypto> {
        val response = service.reqMarketCrypto()
        return response.map { mapper.fromRemote(it) }
    }
}