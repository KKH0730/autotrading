package st.seno.autotrading.data.network.repository

import kotlinx.coroutines.flow.Flow
import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.data.network.model.Result

interface MyAssetsRepository {
    suspend fun reqMyAssets(): Flow<Result<List<Asset>>>
}