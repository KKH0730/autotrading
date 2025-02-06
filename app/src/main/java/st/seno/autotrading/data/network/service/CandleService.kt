package st.seno.autotrading.data.network.service

import retrofit2.http.GET
import retrofit2.http.Query
import st.seno.autotrading.data.network.response_model.CandleResponse

interface CandleService {

    @GET("candles/days")
    suspend fun reqDayCandle(
        @Query("market") market: String,
        @Query("to") to: String,
        @Query("count") count: Int,
        @Query("converting_price_unit") convertingPriceUnit: String
    ): List<CandleResponse>
}