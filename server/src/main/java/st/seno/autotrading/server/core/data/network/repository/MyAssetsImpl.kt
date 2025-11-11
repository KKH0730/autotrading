package st.seno.autotrading.server.core.data.network.repository

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import st.seno.autotrading.server.core.data.mapper.AssetsMapper
import st.seno.autotrading.server.core.data.network.model.Asset
import st.seno.autotrading.server.core.data.network.response_model.MyAssetsResponse

@Repository
class MyAssetsImpl(
    private val webClient: WebClient,
    private val mapper: AssetsMapper
) : MyAssetsRepository {
    override suspend fun reqMyAssets(): List<Asset> {
        val response = webClient.get()
            .uri { uriBuilder -> uriBuilder.path("/accounts").build() }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<MyAssetsResponse>>() {})
            .awaitSingle()

        return response.map { mapper.fromRemote(it) }
    }
}