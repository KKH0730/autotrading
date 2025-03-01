package st.seno.autotrading.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.paging.CandlePagingSourceFactory
import st.seno.autotrading.data.network.paging.FlowUseCase
import st.seno.autotrading.data.network.repository.CandleRepository
import st.seno.autotrading.data.network.response_model.Trade
import st.seno.autotrading.di.Qualifiers
import st.seno.autotrading.extensions.safeCall
import st.seno.autotrading.model.CandleListType
import timber.log.Timber
import javax.inject.Inject

class CandlePagingUseCase @Inject constructor(
    private val candlePagingSourceFactory: CandlePagingSourceFactory,
    @Qualifiers.IoDispatcher private val ioDispatcher: CoroutineDispatcher
): FlowUseCase<CandlePagingUseCase.CandleParams, PagingData<CandleListType>>(dispatcher = ioDispatcher) {

    override fun execute(params: CandleParams): Flow<Result<PagingData<CandleListType>>> {
        return Pager(
            PagingConfig(
                pageSize = params.count,
                prefetchDistance = 5,
                enablePlaceholders = false
            )
        ) {
            candlePagingSourceFactory.create(
                market = params.market,
                to = params.to,
                count = params.count,
                unit = params.unit,
                timeFrame = params.timeFrame,
                trades = params.trades
            )
        }
            .flow
            .map { Result.Success(it) }
    }


    data class CandleParams(
        val market: String,
        val to: String,
        val count: Int,
        val unit: Int?,
        val timeFrame: String,
        val trades: List<Trade>
    )
}

class CandleUseCase @Inject constructor(
    private val candleRepository: CandleRepository,
){
    suspend fun reqDaysCandle(
        market: String,
        to: String?,
        count: Int
    ): Result<List<Candle>> {
        return safeCall {
            candleRepository.reqDaysCandle(
                market = market,
                to = to,
                count = count
            )
        }
    }
}