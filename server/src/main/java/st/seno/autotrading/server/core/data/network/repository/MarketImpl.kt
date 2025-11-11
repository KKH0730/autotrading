package st.seno.autotrading.server.core.data.network.repository

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import st.seno.autotrading.server.core.data.mapper.MarketCryptoMapper
import st.seno.autotrading.server.core.data.network.model.Crypto
import st.seno.autotrading.server.core.data.network.response_model.MarketCryptoResponse

@Repository
class MarketImpl(
    private val webClient: WebClient,
    private val mapper: MarketCryptoMapper

) : MarketRepository {
    override suspend fun reqMarketCrypto(): List<Crypto> {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/market/all")
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<MarketCryptoResponse>>() {})
            .awaitSingle()

        return response.map { mapper.fromRemote(it) }
    }
}