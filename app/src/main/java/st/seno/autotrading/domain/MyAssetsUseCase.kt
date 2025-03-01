package st.seno.autotrading.domain

import kotlinx.coroutines.CoroutineDispatcher
import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.repository.MyAssetsRepository
import st.seno.autotrading.di.Qualifiers
import st.seno.autotrading.extensions.safeCall
import javax.inject.Inject

class MyAssetsUseCase @Inject constructor(
    private val myAssetsRepository: MyAssetsRepository,
    @Qualifiers.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun reqMyAssets(): Result<List<Asset>> = safeCall {
        myAssetsRepository.reqMyAssets()
    }
}