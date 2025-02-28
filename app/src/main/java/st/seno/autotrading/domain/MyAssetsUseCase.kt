package st.seno.autotrading.domain

import st.seno.autotrading.di.Qualifiers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.repository.MyAssetsRepository
import st.seno.autotrading.extensions.catchError
import javax.inject.Inject

class MyAssetsUseCase @Inject constructor(
    private val myAssetsRepository: MyAssetsRepository,
    @Qualifiers.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun reqMyAssets(): Result<List<Asset>> {
        return myAssetsRepository.reqMyAssets().catchError(dispatcher = ioDispatcher)
    }
}