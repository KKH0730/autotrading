package st.seno.autotrading.data.network.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import st.seno.autotrading.data.network.response_model.DaysCandleResponse
import st.seno.autotrading.data.network.response_model.MinutesCandleResponse
import st.seno.autotrading.data.network.response_model.MonthsCandleResponse
import st.seno.autotrading.data.network.response_model.SecondsCandleResponse
import st.seno.autotrading.data.network.response_model.WeeksCandleResponse
import st.seno.autotrading.data.network.response_model.YearsCandleResponse

interface CandleService {

    @GET("candles/years")
    suspend fun reqYearsCandle(
        @Query("market") market: String,
        @Query("to") to: String,
        @Query("count") count: Int,
    ): List<YearsCandleResponse>

    @GET("candles/months")
    suspend fun reqMonthsCandle(
        @Query("market") market: String,
        @Query("to") to: String,
        @Query("count") count: Int,
    ): List<MonthsCandleResponse>

    @GET("candles/weeks")
    suspend fun reqWeeksCandle(
        @Query("market") market: String,
        @Query("to") to: String,
        @Query("count") count: Int,
    ): List<WeeksCandleResponse>

    @GET("candles/days")
    suspend fun reqDaysCandle(
        @Query("market") market: String,
        @Query("to") to: String?,
        @Query("count") count: Int,
        @Query("converting_price_unit") convertingPriceUnit: String
    ): List<DaysCandleResponse>

    @GET("candles/minutes/{unit}")
    suspend fun reqMinutesCandle(
        @Path("unit") unit: Int,
        @Query("market") market: String,
        @Query("to") to: String,
        @Query("count") count: Int,
    ): List<MinutesCandleResponse>

    @GET("candles/seconds")
    suspend fun reqSecondsCandle(
        @Query("market") market: String,
        @Query("to") to: String,
        @Query("count") count: Int,
    ): List<SecondsCandleResponse>
}