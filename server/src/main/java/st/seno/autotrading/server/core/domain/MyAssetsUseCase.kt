package st.seno.autotrading.server.core.domain

import org.springframework.stereotype.Service
import st.seno.autotrading.server.core.data.network.model.Asset
import st.seno.autotrading.server.core.data.network.repository.MyAssetsRepository
import st.seno.autotrading.server.core.extension.safeCall
import st.seno.autotrading.server.core.data.network.model.Result

@Service
class MyAssetsUseCase(
    private val myAssetsRepository: MyAssetsRepository,
) {
    suspend fun reqMyAssets(): Result<List<Asset>> = safeCall {
        myAssetsRepository.reqMyAssets()
    }
}