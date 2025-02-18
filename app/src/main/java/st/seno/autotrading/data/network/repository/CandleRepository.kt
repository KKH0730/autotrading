package st.seno.autotrading.data.network.repository

import kotlinx.coroutines.flow.Flow
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.model.Result

interface CandleRepository  {

    suspend fun reqYearsCandle(
        market: String,
        to: String,
        count: Int,
    ): Flow<Result<List<Candle>>>

    suspend fun reqMonthsCandle(
        market: String,
        to: String,
        count: Int,
    ): Flow<Result<List<Candle>>>

    suspend fun reqWeeksCandle(
        market: String,
        to: String,
        count: Int,
    ): Flow<Result<List<Candle>>>

    suspend fun reqDaysCandle(
        market: String,
        to: String?,
        count: Int,
        convertingPriceUnit: String
    ): Flow<Result<List<Candle>>>

    suspend fun reqMinutesCandle(
        market: String,
        to: String,
        count: Int,
        unit: Int
    ): Flow<Result<List<Candle>>>

    suspend fun reqSecondsCandle(
        market: String,
        to: String,
        count: Int,
    ): Flow<Result<List<Candle>>>
}