package st.seno.autotrading.data.network.service

import retrofit2.http.GET
import st.seno.autotrading.data.network.response_model.MarketCryptoResponse

interface MarketService {
    @GET("market/all")
    suspend fun reqMarketCrypto(): List<MarketCryptoResponse>
}