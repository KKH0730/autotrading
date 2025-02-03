package st.seno.autotrading.data.network.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import st.seno.autotrading.data.mapper.AssetsMapper
import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.service.MyAssetsService
import timber.log.Timber
import javax.inject.Inject


class MyAssetsImpl @Inject constructor(
    private val service: MyAssetsService,
    private val mapper: AssetsMapper
) : MyAssetsRepository {
    override suspend fun reqMyAssets(): Flow<Result<List<Asset>>> {
        return flow {
            repeat(3) { count ->
                runCatching {
                    val response = service.reqMyAssets()
                    emit(Result.Success(response.map { mapper.fromRemote(it) }))
                    return@flow
                }.onFailure {
                    if (count == 2) {
                        emit(Result.Error(it))
                    }
                }
            }
        }
    }
}