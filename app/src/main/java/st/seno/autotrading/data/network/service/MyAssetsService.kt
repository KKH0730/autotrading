package st.seno.autotrading.data.network.service

import retrofit2.http.GET
import st.seno.autotrading.data.network.response_model.MyAssetsResponse


interface MyAssetsService {
    @GET("accounts")
    suspend fun reqMyAssets(): List<MyAssetsResponse>
}