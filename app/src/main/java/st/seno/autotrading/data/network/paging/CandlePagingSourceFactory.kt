package st.seno.autotrading.data.network.paging

import st.seno.autotrading.data.network.repository.CandleRepository
import st.seno.autotrading.data.network.response_model.Trade
import javax.inject.Inject

class CandlePagingSourceFactory @Inject constructor(
    private val candleRepository: CandleRepository
) {
    fun create(
        market: String,
        to: String,
        count: Int,
        unit: Int?,
        timeFrame: String,
        trades: List<Trade>
    ) = CandlePagingSource(
        market = market,
        to = to,
        count = count,
        unit = unit,
        timeFrame = timeFrame,
        trades = trades,
        candleRepository = candleRepository
    )
}