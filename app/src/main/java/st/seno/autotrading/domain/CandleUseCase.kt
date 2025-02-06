package st.seno.autotrading.domain

import com.keytalkai.lewis.di.Qualifiers
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
    suspend fun reqDayCandle(
        market: String,
        to: String,
        count: Int,
        convertingPriceUnit: String
    ): Flow<Result<List<Candle>>> {
        return candleRepository.reqDayCandle(
            market = market,
            to = to,
            count = count,
            convertingPriceUnit = convertingPriceUnit
        ).catchError(dispatcher = ioDispatcher)
    }
}