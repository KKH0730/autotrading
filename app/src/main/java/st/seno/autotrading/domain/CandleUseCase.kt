package st.seno.autotrading.domain

import st.seno.autotrading.di.Qualifiers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.repository.CandleRepository
import st.seno.autotrading.extensions.catchError
import javax.inject.Inject

class CandleUseCase @Inject constructor(
    private val candleRepository: CandleRepository,
    @Qualifiers.IoDispatcher private val ioDispatcher: CoroutineDispatcher
){
    suspend fun reqYearsCandle(
        market: String,
        to: String,
        count: Int
    ): Flow<Result<List<Candle>>> {
        return candleRepository.reqYearsCandle(
            market = market,
            to = to,
            count = count
        ).catchError(dispatcher = ioDispatcher)
    }

    suspend fun reqMonthsCandle(
        market: String,
        to: String,
        count: Int
    ): Flow<Result<List<Candle>>> {
        return candleRepository.reqMonthsCandle(
            market = market,
            to = to,
            count = count
        ).catchError(dispatcher = ioDispatcher)
    }

    suspend fun reqWeeksCandle(
        market: String,
        to: String,
        count: Int
    ): Flow<Result<List<Candle>>> {
        return candleRepository.reqWeeksCandle(
            market = market,
            to = to,
            count = count
        ).catchError(dispatcher = ioDispatcher)
    }

    suspend fun reqDaysCandle(
        market: String,
        to: String?,
        count: Int,
        convertingPriceUnit: String
    ): Flow<Result<List<Candle>>> {
        return candleRepository.reqDaysCandle(
            market = market,
            to = to,
            count = count,
            convertingPriceUnit = convertingPriceUnit
        ).catchError(dispatcher = ioDispatcher)
    }

    suspend fun reqMinutesCandle(
        market: String,
        to: String,
        count: Int,
        unit: Int
    ): Flow<Result<List<Candle>>> {
        return candleRepository.reqMinutesCandle(
            market = market,
            to = to,
            count = count,
            unit = unit
        ).catchError(dispatcher = ioDispatcher)
    }

    suspend fun reqSecondsCandle(
        market: String,
        to: String,
        count: Int
    ): Flow<Result<List<Candle>>> {
        return candleRepository.reqSecondsCandle(
            market = market,
            to = to,
            count = count
        ).catchError(dispatcher = ioDispatcher)
    }
}