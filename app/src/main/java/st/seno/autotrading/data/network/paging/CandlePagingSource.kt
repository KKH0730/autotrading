package st.seno.autotrading.data.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.repository.CandleRepository
import st.seno.autotrading.data.network.response_model.Trade
import st.seno.autotrading.model.CandleListType
import st.seno.autotrading.ui.main.trading_view.candleTimeFrames
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CandlePagingSource(
    private val market: String,
    private val to: String,
    private val count: Int,
    private val unit: Int?,
    private val timeFrame: String,
    private val trades: List<Trade>,
    private val candleRepository: CandleRepository
) : PagingSource<String, CandleListType>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, CandleListType> = try {
        val key = params.key ?: to

        val candleListTypes: MutableList<CandleListType> = when (timeFrame) {
            candleTimeFrames[0].first, candleTimeFrames[1].first, candleTimeFrames[2].first -> candleRepository.reqMinutesCandle(market = market, to = key, count = count, unit = unit)
            candleTimeFrames[3].first -> candleRepository.reqDaysCandle(market = market, to = key, count = count)
            candleTimeFrames[4].first -> candleRepository.reqWeeksCandle(market = market, to = key, count = count)
            candleTimeFrames[5].first -> candleRepository.reqMonthsCandle(market = market, to = key, count = count)
            else -> candleRepository.reqYearsCandle(market = market, to = key, count = count)
        }
            .updateCandleSidesFromTrades()
            .toMutableList()
            .also { if (params.key == null) it.add(0, CandleListType.Blank()) }

        var nextKey = ""
        candleListTypes.lastOrNull()
            ?.takeIf { it is CandleListType.CandleType }
            ?.let { candleListType ->
                val candleType = candleListType as CandleListType.CandleType
                val date: LocalDateTime = LocalDateTime.parse(candleType.candle.candleDateTimeUtc, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                val updatedDate = when (timeFrame) {
                    candleTimeFrames[0].first -> date.minusMinutes(1)
                    candleTimeFrames[1].first -> date.minusMinutes(15)
                    candleTimeFrames[2].first -> date.minusMinutes(60)
                    candleTimeFrames[3].first -> date.minusDays(1)
                    candleTimeFrames[4].first -> date.minusDays(7)
                    candleTimeFrames[5].first -> date.minusMonths(1)
                    else -> date.minusYears(1)
                }
                nextKey = updatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            }

        if (nextKey.isEmpty()) candleListTypes.add(CandleListType.Blank())

        LoadResult.Page(
            data = candleListTypes,
            prevKey = null,
            nextKey = if (key == nextKey || nextKey.isEmpty()) null else nextKey
        )
    } catch (e: Exception) {
        Timber.e(e)
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<String, CandleListType>): String? = null

    private fun List<Candle>.updateCandleSidesFromTrades(): List<CandleListType> {
        trades.forEach { trade ->
            val targetCandleTime = LocalDateTime.parse(trade.tradesCreatedAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            val index = this.indexOfFirst { candle ->
                val currentCandleTime = LocalDateTime.parse(candle.candleDateTimeUtc, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                when (timeFrame) {
                    candleTimeFrames[0].first -> {
                        targetCandleTime.year == currentCandleTime.year
                                && targetCandleTime.monthValue == currentCandleTime.monthValue
                                && targetCandleTime.dayOfMonth == currentCandleTime.dayOfMonth
                                && targetCandleTime.hour == currentCandleTime.hour
                                && targetCandleTime.minute == currentCandleTime.minute
                    }

                    candleTimeFrames[1].first -> {
                        val nextCandleTime = currentCandleTime.plusMinutes(15)
                        (currentCandleTime.isBefore(targetCandleTime) && nextCandleTime.isAfter(targetCandleTime)) ||
                                targetCandleTime.year == currentCandleTime.year
                                && targetCandleTime.monthValue == currentCandleTime.monthValue
                                && targetCandleTime.dayOfMonth == currentCandleTime.dayOfMonth
                                && targetCandleTime.hour == currentCandleTime.hour
                                && targetCandleTime.minute == currentCandleTime.minute
                    }

                    candleTimeFrames[2].first -> {
                        val nextCandleTime = currentCandleTime.plusMinutes(60)
                        (currentCandleTime.isBefore(targetCandleTime) && nextCandleTime.isAfter(targetCandleTime)) ||
                                targetCandleTime.year == currentCandleTime.year
                                && targetCandleTime.monthValue == currentCandleTime.monthValue
                                && targetCandleTime.dayOfMonth == currentCandleTime.dayOfMonth
                                && targetCandleTime.hour == currentCandleTime.hour
                                && targetCandleTime.minute == currentCandleTime.minute
                    }

                    candleTimeFrames[3].first, candleTimeFrames[4].first -> {
                        targetCandleTime.year == currentCandleTime.year
                                && targetCandleTime.monthValue == currentCandleTime.monthValue
                                && targetCandleTime.dayOfMonth == currentCandleTime.dayOfMonth
                    }

                    candleTimeFrames[5].first -> {
                        targetCandleTime.year == currentCandleTime.year
                                && targetCandleTime.monthValue == currentCandleTime.monthValue
                    }

                    else -> {
                        targetCandleTime.year == currentCandleTime.year
                    }
                }
            }

            if (index != -1) {
                this[index].side = trade.tradesSide
            }
        }
        return this.map { CandleListType.CandleType(candle = it) }
    }
}