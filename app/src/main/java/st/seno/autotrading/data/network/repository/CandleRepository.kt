package st.seno.autotrading.data.network.repository

import kotlinx.coroutines.flow.Flow
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.model.Result

interface CandleRepository  {
    suspend fun reqDayCandle(
        market: String,
        to: String,
        count: Int,
        convertingPriceUnit: String
    ): Flow<Result<List<Candle>>>
}