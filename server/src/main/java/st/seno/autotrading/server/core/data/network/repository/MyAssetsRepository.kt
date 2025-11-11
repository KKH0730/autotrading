package st.seno.autotrading.server.core.data.network.repository

import org.springframework.stereotype.Repository
import st.seno.autotrading.server.core.data.network.model.Asset

@Repository
interface MyAssetsRepository {
    suspend fun reqMyAssets(): List<Asset>
}