package st.seno.autotrading.data.network.repository

import st.seno.autotrading.data.mapper.DaysCandleMapper
import st.seno.autotrading.data.mapper.MinutesCandleMapper
import st.seno.autotrading.data.mapper.MonthsCandleMapper
import st.seno.autotrading.data.mapper.SecondsCandleMapper
import st.seno.autotrading.data.mapper.WeeksCandleMapper
import st.seno.autotrading.data.mapper.YearsCandleMapper
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.service.CandleService
import javax.inject.Inject

class CandleImpl @Inject constructor(
    private val candleService: CandleService,
    private val yearsCandleMapper: YearsCandleMapper,
    private val monthsCandleMapper: MonthsCandleMapper,
    private val weeksCandleMapper: WeeksCandleMapper,
    private val daysCandleMapper: DaysCandleMapper,
    private val minutesCandleMapper: MinutesCandleMapper,
    private val secondsCandleMapper: SecondsCandleMapper,
) : CandleRepository {
    override suspend fun reqYearsCandle(
        market: String,
        to: String,
        count: Int
    ): List<Candle> {
        val response = candleService.reqYearsCandle(
            market = market,
            to = to,
            count = count
        )
        return response.map { yearsCandleMapper.fromRemote(it) }
    }

    override suspend fun reqMonthsCandle(
        market: String,
        to: String,
        count: Int
    ): List<Candle> {
        val response = candleService.reqMonthsCandle(
            market = market,
            to = to,
            count = count
        )
        return response.map { monthsCandleMapper.fromRemote(it) }
    }

    override suspend fun reqWeeksCandle(
        market: String,
        to: String,
        count: Int
    ): List<Candle> {
        val response = candleService.reqWeeksCandle(
            market = market,
            to = to,
            count = count
        )
        return response.map { weeksCandleMapper.fromRemote(it) }
    }

    override suspend fun reqDaysCandle(
        market: String,
        to: String?,
        count: Int,
    ): List<Candle> {
        val response = candleService.reqDaysCandle(
            market = market,
            to = to,
            count = count,
            convertingPriceUnit = null
        )
        return response.map { daysCandleMapper.fromRemote(it) }
    }

    override suspend fun reqMinutesCandle(
        market: String,
        to: String,
        count: Int,
        unit: Int?
    ): List<Candle> {
        val response = candleService.reqMinutesCandle(
            market = market,
            to = to,
            count = count,
            unit = unit
        )
        return response.map { minutesCandleMapper.fromRemote(it) }
    }

    override suspend fun reqSecondsCandle(
        market: String,
        to: String,
        count: Int
    ): List<Candle> {
        val response = candleService.reqSecondsCandle(
            market = market,
            to = to,
            count = count,
        )
        return response.map { secondsCandleMapper.fromRemote(it) }
    }
}