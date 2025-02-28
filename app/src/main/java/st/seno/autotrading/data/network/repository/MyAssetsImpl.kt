package st.seno.autotrading.data.network.repository

import st.seno.autotrading.data.mapper.AssetsMapper
import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.data.network.service.MyAssetsService
import javax.inject.Inject

class MyAssetsImpl @Inject constructor(
    private val service: MyAssetsService,
    private val mapper: AssetsMapper
) : MyAssetsRepository {
    override suspend fun reqMyAssets(): List<Asset> {
        val response = service.reqMyAssets()
        return response.map { mapper.fromRemote(it) }
    }
}