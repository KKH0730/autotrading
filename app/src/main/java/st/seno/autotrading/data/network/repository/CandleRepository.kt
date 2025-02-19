package st.seno.autotrading.data.network.repository

import st.seno.autotrading.data.network.model.Candle

interface CandleRepository  {

    suspend fun reqYearsCandle(
        market: String,
        to: String,
        count: Int,
    ): List<Candle>

    suspend fun reqMonthsCandle(
        market: String,
        to: String,
        count: Int,
    ): List<Candle>

    suspend fun reqWeeksCandle(
        market: String,
        to: String,
        count: Int,
    ): List<Candle>

    suspend fun reqDaysCandle(
        market: String,
        to: String?,
        count: Int
    ): List<Candle>

    suspend fun reqMinutesCandle(
        market: String,
        to: String,
        count: Int,
        unit: Int?
    ): List<Candle>

    suspend fun reqSecondsCandle(
        market: String,
        to: String,
        count: Int,
    ): List<Candle>
}