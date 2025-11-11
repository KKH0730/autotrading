package st.seno.autotrading.server.core.data.network.repository

import org.springframework.stereotype.Repository
import st.seno.autotrading.server.core.data.network.model.Candle

@Repository
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