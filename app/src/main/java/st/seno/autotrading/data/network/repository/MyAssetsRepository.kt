package st.seno.autotrading.data.network.repository

import st.seno.autotrading.data.network.model.Asset

interface MyAssetsRepository {
    suspend fun reqMyAssets(): List<Asset>
}