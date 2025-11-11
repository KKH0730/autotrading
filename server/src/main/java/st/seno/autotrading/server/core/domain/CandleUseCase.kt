package st.seno.autotrading.server.core.domain

import org.springframework.stereotype.Service
import st.seno.autotrading.server.core.extension.safeCall
import st.seno.autotrading.server.core.data.network.model.Result
import st.seno.autotrading.server.core.data.network.model.Candle
import st.seno.autotrading.server.core.data.network.repository.CandleRepository

@Service
class CandleUseCase(
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