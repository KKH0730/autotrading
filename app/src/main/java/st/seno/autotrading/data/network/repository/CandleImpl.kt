package st.seno.autotrading.data.network.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import st.seno.autotrading.data.mapper.DaysCandleMapper
import st.seno.autotrading.data.mapper.MinutesCandleMapper
import st.seno.autotrading.data.mapper.MonthsCandleMapper
import st.seno.autotrading.data.mapper.SecondsCandleMapper
import st.seno.autotrading.data.mapper.WeeksCandleMapper
import st.seno.autotrading.data.mapper.YearsCandleMapper
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.service.CandleService
import st.seno.autotrading.extensions.retry
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
    ): Flow<Result<List<Candle>>> {
        return flow {
            val response = candleService.reqYearsCandle(
                market = market,
                to = to,
                count = count
            )
            emit(Result.Success(response.map { yearsCandleMapper.fromRemote(it) }))
        }.retry()
    }

    override suspend fun reqMonthsCandle(
        market: String,
        to: String,
        count: Int
    ): Flow<Result<List<Candle>>> {
        return flow {
            val response = candleService.reqMonthsCandle(
                market = market,
                to = to,
                count = count
            )
            emit(Result.Success(response.map { monthsCandleMapper.fromRemote(it) }))
        }.retry()
    }

    override suspend fun reqWeeksCandle(
        market: String,
        to: String,
        count: Int
    ): Flow<Result<List<Candle>>> {
        return flow {
            val response = candleService.reqWeeksCandle(
                market = market,
                to = to,
                count = count
            )
            emit(Result.Success(response.map { weeksCandleMapper.fromRemote(it) }))
        }.retry()
    }

    override suspend fun reqDaysCandle(
        market: String,
        to: String,
        count: Int,
        convertingPriceUnit: String
    ): Flow<Result<List<Candle>>> {
        return flow {
            val response = candleService.reqDaysCandle(
                market = market,
                to = to,
                count = count,
                convertingPriceUnit = convertingPriceUnit
            )
            emit(Result.Success(response.map { daysCandleMapper.fromRemote(it) }))
        }.retry()
    }

    override suspend fun reqMinutesCandle(
        market: String,
        to: String,
        count: Int,
        unit: Int
    ): Flow<Result<List<Candle>>> {
        return flow {
            val response = candleService.reqMinutesCandle(
                market = market,
                to = to,
                count = count,
                unit = unit
            )
            emit(Result.Success(response.map { minutesCandleMapper.fromRemote(it) }))
        }.retry()
    }

    override suspend fun reqSecondsCandle(
        market: String,
        to: String,
        count: Int
    ): Flow<Result<List<Candle>>> {
        return flow {
            val response = candleService.reqSecondsCandle(
                market = market,
                to = to,
                count = count
            )
            emit(Result.Success(response.map { secondsCandleMapper.fromRemote(it) }))
        }.retry()
    }
}