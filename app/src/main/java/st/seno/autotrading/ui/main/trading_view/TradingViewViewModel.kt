package st.seno.autotrading.ui.main.trading_view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.data.network.response_model.Trade
import st.seno.autotrading.domain.CandlePagingUseCase
import st.seno.autotrading.domain.TradingDataUseCase
import st.seno.autotrading.extensions.formatedDate
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.ui.base.BaseViewModel
import st.seno.autotrading.ui.main.MainViewModel
import st.seno.autotrading.util.BookmarkUtil
import st.seno.autotrading.util.BookmarkUtil.bookmarkedTickers
import java.time.LocalDateTime
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
    private val candlePagingUseCase: CandlePagingUseCase,
    private val tradingDataUseCase: TradingDataUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val marketId = savedStateHandle[TradingViewActivity.TICKER_CODE] ?: ""
    private val autoTradingStartDate = savedStateHandle[TradingViewActivity.AUTO_TRADING_START_DATE] ?: ""

    private val _candleParams: MutableStateFlow<CandlePagingUseCase.CandleParams> = MutableStateFlow(
        CandlePagingUseCase.CandleParams(
            market = marketId,
            to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
            count = 200,
            unit = null,
            timeFrame = (candleTimeFrames[3].first)
        )
    )
    private val candleParams: StateFlow<CandlePagingUseCase.CandleParams> get() = _candleParams.asStateFlow()

    private val _trades: MutableStateFlow<List<Trade>> = MutableStateFlow(listOf())
    val trades: StateFlow<List<Trade>> get() = _trades.asStateFlow()

    val candleChartModel: StateFlow<CandleChartModel?> = combine(MainViewModel.tickersMap, bookmarkedTickers) { tickersMap, bookmarkedTickers ->
        val currentTicker: Ticker? = tickersMap[marketId]
        val favoriteTickers = BookmarkUtil.convertSetToTickerList(bookmarkedTickerCodeSet = bookmarkedTickers)
//        val firstOfDayList = candles.mapIndexedNotNull { index, candle -> if (candle.candleDateTimeUtc.isFirstDayOfMonth()) index else null }
//        val candleBodyWidth = 5.0
//        val firstOfDayOffsetList = firstOfDayList.foldIndexed(listOf()) { index: Int, acc: List<Double>, firstOfDayIndex: Int ->
//            val date = candles[firstOfDayIndex].candleDateTimeUtc.parseDateFormat(
//                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
//                outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//            )
//            val prevDate = if (index != 0) {
//                candles[firstOfDayList[index]].candleDateTimeUtc.parseDateFormat(
//                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
//                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//                )
//            } else ""
//
//            val textWidth = date.measureTextWidth(fontSizeSp = 8f).pxToDp()
//            val prevTextWidth = prevDate.measureTextWidth(fontSizeSp = 8f).pxToDp()
//            val result = arrayListOf<Double>().apply {
//                addAll(acc)
//            }
//            if (index == 0) {
//                (0..firstOfDayIndex).forEach { _ ->
//                    result.add((candleBodyWidth / 2) - (textWidth / 2.0))
////                        result.add((candleBodyWidth / 2) + (-textWidth / 2.0))
//                }
//            } else {
////                val offset = -(prevTextWidth - candleBodyWidth - candleSpace)
//                val offset = -(prevTextWidth - candleBodyWidth - candleSpace) - (textWidth / 2.0)
//                ((acc.lastIndex + 1)..firstOfDayIndex).forEach { _ ->
//                    result.add(offset)
//                }
//
//                if (index == firstOfDayList.lastIndex && firstOfDayIndex < candles.lastIndex) {
//                    val finalOffset = (result.lastOrNull() ?: 0.0) - (textWidth / 2.0)
//                    ((firstOfDayIndex + 1) .. candles.lastIndex).forEach { _ ->
//                        result.add(0.0)
//                       }
//                }
//            }
//            result
//        }

//        updateCandleSidesFromTrades(candles = candles, trades = trades.value, timeFrame = timeFrame.value)

        CandleChartModel(
            ticker = currentTicker,
            favoriteTickers = favoriteTickers,
            firstOfDayOffsetList = listOf()
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val rawCandles = candleParams.flatMapLatest { params ->
        candlePagingUseCase.execute(params = params)
            .map { result ->
                when (result) {
                    is Result.Success -> { result.data }
                    else -> PagingData.empty()
                }
            }
    }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    init {
        if (autoTradingStartDate.isNotEmpty()) {
            reqTradingData(startDate = autoTradingStartDate)
        }
    }

//    val candles = combine(rawCandles, _trades) { rawCandles, trades ->
//        rawCandles.map { pagingData ->
//            val modifiedCandles = updateCandleSidesFromTrades(pagingData.toList(), trades, _candleParams.value.type)
//            PagingData.from(modifiedCandles) // 변환된 데이터로 새롭게 PagingData 생성
//        }
//    }.stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())


    fun reqCandles(timeFrameName: String, unit: Int = 0) {
        _candleParams.value = when(timeFrameName) {
            candleTimeFrames[0].first, candleTimeFrames[1].first, candleTimeFrames[2].first -> {
                CandlePagingUseCase.CandleParams(
                    market = marketId,
                    to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                    count = 200,
                    unit = unit,
                    timeFrame = timeFrameName
                )
            }
            candleTimeFrames[3].first -> {
                CandlePagingUseCase.CandleParams(
                    market = marketId,
                    to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                    count = 200,
                    unit = null,
                    timeFrame = timeFrameName
                )
            }
            candleTimeFrames[4].first -> {
                CandlePagingUseCase.CandleParams(
                    market = marketId,
                    to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                    count = 200,
                    unit = null,
                    timeFrame = timeFrameName
                )
            }
            candleTimeFrames[5].first -> {
                CandlePagingUseCase.CandleParams(
                    market = marketId,
                    to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                    count = 200,
                    unit = null,
                    timeFrame = timeFrameName
                )
            }
            else -> {
                CandlePagingUseCase.CandleParams(
                    market = marketId,
                    to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                    count = 200,
                    unit = null,
                    timeFrame = timeFrameName
                )
            }
        }
    }

    private fun reqTradingData(startDate: String) {
        vmScopeJob {
            tradingDataUseCase.reqTradingData(startDate = startDate).collectLatest {
                _trades.value = it.reversed().flatMap { trade -> trade.order.trades ?: listOf() }
            }
        }
    }

    private fun updateCandleSidesFromTrades(
        candles: List<Candle>,
        trades: List<Trade>,
        timeFrame: String
    ): List<Candle> {
        trades.forEach { trade ->
            val targetCandleTime = LocalDateTime.parse(trade.tradesCreatedAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            val index = candles.indexOfFirst { candle ->
                val currentCandleTime = LocalDateTime.parse(candle.candleDateTimeUtc, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                when(timeFrame) {
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
                    else -> { targetCandleTime.year == currentCandleTime.year }
                }
            }

            if (index != -1) {
                candles[index].side = trade.tradesSide
            }
        }
        return candles
    }
}

data class CandleChartModel(
    val ticker: Ticker?,
    val favoriteTickers: List<Ticker>,
    val firstOfDayOffsetList: List<Double>
)