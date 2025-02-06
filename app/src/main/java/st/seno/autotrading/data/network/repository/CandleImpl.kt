package st.seno.autotrading.data.network.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import st.seno.autotrading.data.mapper.CandleMapper
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.service.CandleService
import javax.inject.Inject

class CandleImpl @Inject constructor(
    private val candleService: CandleService,
    private val mapper: CandleMapper
) : CandleRepository {
    override suspend fun reqDayCandle(
        market: String,
        to: String,
        count: Int,
        convertingPriceUnit: String
    ): Flow<Result<List<Candle>>> {
        return flow {
            val response = candleService.reqDayCandle(
                market = market,
                to = to,
                count = count,
                convertingPriceUnit = convertingPriceUnit
            )
            emit(Result.Success(response.map { mapper.fromRemote(it) }))
        }
    }
}