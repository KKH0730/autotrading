package st.seno.autotrading.ui.main.trading_view

import androidx.compose.ui.util.fastForEachIndexed
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.data.network.model.isSuccess
import st.seno.autotrading.data.network.model.successData
import st.seno.autotrading.data.network.socket.RxSocketClient
import st.seno.autotrading.domain.CandleUseCase
import st.seno.autotrading.extensions.formatedDate
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.extensions.isFirstDayOfMonth
import st.seno.autotrading.extensions.measureTextWidth
import st.seno.autotrading.extensions.parseDateFormat
import st.seno.autotrading.extensions.pxToDp
import st.seno.autotrading.ui.base.BaseViewModel
import st.seno.autotrading.ui.main.MainViewModel
import st.seno.autotrading.util.BookmarkUtil
import st.seno.autotrading.util.BookmarkUtil.bookmarkedTickers
import timber.log.Timber
import java.time.format.DateTimeFormatter
import javax.inject.Inject

val candleSpace = 1
val candleTimeFrames = listOf(
    getString(R.string.trading_view_time_frame_1m) to 1,
    getString(R.string.trading_view_time_frame_15m) to 15,
    getString(R.string.trading_view_time_frame_1h) to 60,
    getString(R.string.trading_view_time_frame_1d) to -1,
    getString(R.string.trading_view_time_frame_1w) to -1,
    getString(R.string.trading_view_time_frame_1_month) to -1,
    getString(R.string.trading_view_time_frame_1y) to -1,
)

fun String.isDaysCandle() = this == candleTimeFrames[3].first

@HiltViewModel
class TradingViewViewModel @Inject constructor(
    private val candleUseCase: CandleUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val marketId = savedStateHandle[TradingViewActivity.TICKER_CODE] ?: ""
    private val tradingViewCandlesMap = mutableMapOf<String, List<Candle>>()

    private val tradingViewCandles: MutableStateFlow<List<Candle>> = MutableStateFlow(listOf())

    val candleChartModel: StateFlow<CandleChartModel?> = combine(MainViewModel.tickersMap, tradingViewCandles, bookmarkedTickers) { tickersMap, candles, bookmarkedTickers ->
        val currentTicker: Ticker? = tickersMap[marketId]
        val favoriteTickers = BookmarkUtil.convertSetToTickerList(bookmarkedTickerCodeSet = bookmarkedTickers)
        val firstOfDayList = candles.mapIndexedNotNull { index, candle -> if (candle.candleDateTimeUtc.isFirstDayOfMonth()) index else null }
        val candleBodyWidth = 5.0
        val firstOfDayOffsetList = firstOfDayList.foldIndexed(listOf()) { index: Int, acc: List<Double>, firstOfDayIndex: Int ->
            val date = candles[firstOfDayIndex].candleDateTimeUtc.parseDateFormat(
                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val prevDate = if (index != 0) {
                candles[firstOfDayList[index]].candleDateTimeUtc.parseDateFormat(
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
            } else ""

            val textWidth = date.measureTextWidth(fontSizeSp = 8f).pxToDp()
            val prevTextWidth = prevDate.measureTextWidth(fontSizeSp = 8f).pxToDp()
            val result = arrayListOf<Double>().apply {
                addAll(acc)
            }
            if (index == 0) {
                (0..firstOfDayIndex).forEach { _ ->
                    result.add((candleBodyWidth / 2) - (textWidth / 2.0))
//                        result.add((candleBodyWidth / 2) + (-textWidth / 2.0))
                }
            } else {
//                val offset = -(prevTextWidth - candleBodyWidth - candleSpace)
                val offset = -(prevTextWidth - candleBodyWidth - candleSpace) - (textWidth / 2.0)
                ((acc.lastIndex + 1)..firstOfDayIndex).forEach { _ ->
                    result.add(offset)
                }

                if (index == firstOfDayList.lastIndex && firstOfDayIndex < candles.lastIndex) {
                    val finalOffset = (result.lastOrNull() ?: 0.0) - (textWidth / 2.0)
                    ((firstOfDayIndex + 1) .. candles.lastIndex).forEach { _ ->
                        result.add(0.0)
                       }
                }
            }
            result
        }

        CandleChartModel(
            ticker = currentTicker,
            favoriteTickers = favoriteTickers,
            candles = candles,
            firstOfDayOffsetList = firstOfDayOffsetList
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    init {
        reqCandles(timeFrameName = candleTimeFrames[3].first)
    }

    fun reqCandles(timeFrameName: String, unit: Int = 0) {
        vmScopeJob {
            tradingViewCandlesMap[timeFrameName]?.let {
                tradingViewCandles.value = it
                return@vmScopeJob
            }

            showLoading()

            when(timeFrameName) {
                candleTimeFrames[6].first -> {
                    candleUseCase.reqYearsCandle(
                        market = marketId,
                        to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                        count = 200
                    )
                }
                candleTimeFrames[5].first -> {
                    candleUseCase.reqMonthsCandle(
                        market = marketId,
                        to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                        count = 200
                    )
                }
                candleTimeFrames[4].first -> {
                    candleUseCase.reqWeeksCandle(
                        market = marketId,
                        to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                        count = 200
                    )
                }
                candleTimeFrames[3].first -> {
                    candleUseCase.reqDaysCandle(
                        market = marketId,
                        to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                        count = 200,
                        convertingPriceUnit = "KRW"
                    )
                }
                candleTimeFrames[2].first -> {
                    candleUseCase.reqMinutesCandle(
                        market = marketId,
                        to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                        count = 200,
                        unit = unit
                    )
                }
                candleTimeFrames[1].first -> {
                    candleUseCase.reqMinutesCandle(
                        market = marketId,
                        to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                        count = 200,
                        unit = unit
                    )
                }
                candleTimeFrames[0].first -> {
                    candleUseCase.reqMinutesCandle(
                        market = marketId,
                        to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                        count = 200,
                        unit = unit
                    )
                }
                else -> { null }
            }?.collectLatest {
                if (it.isSuccess()) {
                    tradingViewCandles.value = it.successData()
                    tradingViewCandlesMap[timeFrameName] = it.successData()
                } else {
                    showSnackbar(message = getString(R.string.network_error))
                }
                hideLoading()
            } ?: hideLoading()
        }
    }
}

data class CandleChartModel(
    val ticker: Ticker?,
    val favoriteTickers: List<Ticker>,
    val candles: List<Candle>,
    val firstOfDayOffsetList: List<Double>
)