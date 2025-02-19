package st.seno.autotrading.data.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.repository.CandleRepository
import st.seno.autotrading.ui.main.trading_view.candleTimeFrames
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val INITIAL_INDEX = 0

class CandlePagingSource(
    private val market: String,
    private val to: String,
    private val count: Int,
    private val unit: Int?,
    private val timeFrame: String,
    private val candleRepository: CandleRepository
) : PagingSource<String, Candle>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Candle> = try {
        val key = params.key ?: to

        val candles = when(timeFrame) {
            candleTimeFrames[0].first, candleTimeFrames[1].first, candleTimeFrames[2].first -> {
                candleRepository.reqMinutesCandle(
                    market = market,
                    to = key,
                    count = count,
                    unit = unit
                )
            }
            candleTimeFrames[3].first -> {
                candleRepository.reqDaysCandle(
                    market = market,
                    to = key,
                    count = count
                )
            }
            candleTimeFrames[4].first -> {
                candleRepository.reqWeeksCandle(
                    market = market,
                    to = key,
                    count = count
                )
            }
            candleTimeFrames[5].first -> {
                candleRepository.reqMonthsCandle(
                    market = market,
                    to = key,
                    count = count
                )
            }
            else -> {
                candleRepository.reqYearsCandle(
                    market = market,
                    to = key,
                    count = count
                )
            }
        }
        var nextKey = ""
        candles.lastOrNull()?.let { candle ->
            val date: LocalDateTime = LocalDateTime.parse(candle.candleDateTimeUtc, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val updatedDate = when(timeFrame) {
                candleTimeFrames[0].first -> { date.minusMinutes(1) }
                candleTimeFrames[1].first -> {  date.minusMinutes(15) }
                candleTimeFrames[2].first -> {  date.minusMinutes(60) }
                candleTimeFrames[3].first -> {  date.minusDays(1) }
                candleTimeFrames[4].first -> {  date.minusDays(7) }
                candleTimeFrames[5].first -> {  date.minusMonths(1) }
                else -> date.minusYears(1)
            }
            nextKey = updatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        }
        LoadResult.Page(
            data = candles,
            prevKey = null,
            nextKey = if (key == nextKey || nextKey.isEmpty()) null else nextKey
        )
    } catch (e: Exception) {
        Timber.e(e)
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<String, Candle>): String? = null
}