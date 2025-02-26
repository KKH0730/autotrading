package st.seno.autotrading.ui.main.trading_view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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
import st.seno.autotrading.model.CandleListType
import st.seno.autotrading.ui.base.BaseViewModel
import st.seno.autotrading.ui.main.MainViewModel
import st.seno.autotrading.util.BookmarkUtil
import st.seno.autotrading.util.BookmarkUtil.bookmarkedTickers
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

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

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TradingViewViewModel @Inject constructor(
    private val candlePagingUseCase: CandlePagingUseCase,
    private val tradingDataUseCase: TradingDataUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val marketId = savedStateHandle[TradingViewActivity.TICKER_CODE] ?: ""
    private val autoTradingStartDate = savedStateHandle[TradingViewActivity.AUTO_TRADING_START_DATE] ?: ""

    private val _candleParams: MutableStateFlow<CandlePagingUseCase.CandleParams?> = MutableStateFlow(null)
    private val candleParams: StateFlow<CandlePagingUseCase.CandleParams?> get() = _candleParams.asStateFlow()

    private val _trades: MutableStateFlow<List<Trade>> = MutableStateFlow(listOf())
    val trades: StateFlow<List<Trade>> get() = _trades.asStateFlow()

    val candleChartModel: StateFlow<CandleChartModel?> = combine(MainViewModel.tickersMap, bookmarkedTickers) { tickersMap, bookmarkedTickers ->
        val currentTicker: Ticker? = tickersMap[marketId]
        val favoriteTickers = BookmarkUtil.convertSetToTickerList(bookmarkedTickerCodeSet = bookmarkedTickers)

        CandleChartModel(
            ticker = currentTicker,
            favoriteTickers = favoriteTickers,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val rawCandles = candleParams.flatMapLatest { params ->
        if (params != null) {
            candlePagingUseCase.execute(params = params)
                .map { result ->
                    when (result) {
                        is Result.Success -> { result.data }
                        else -> PagingData.empty()
                    }
                }
        } else {
            flow {  }
        }
    }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    init {
        if (autoTradingStartDate.isNotEmpty()) {
            reqTradingData(startDate = autoTradingStartDate)
        } else {
            _candleParams.value = CandlePagingUseCase.CandleParams(
                market = marketId,
                to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                count = 200,
                unit = null,
                timeFrame = (candleTimeFrames[3].first),
                trades = listOf()
            )
        }
    }


    fun reqCandles(timeFrameName: String, unit: Int = 0) {
        _candleParams.value = when(timeFrameName) {
            candleTimeFrames[0].first, candleTimeFrames[1].first, candleTimeFrames[2].first -> {
                CandlePagingUseCase.CandleParams(
                    market = marketId,
                    to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                    count = 200,
                    unit = unit,
                    timeFrame = timeFrameName,
                    trades = trades.value,
                )
            }
            candleTimeFrames[3].first -> {
                CandlePagingUseCase.CandleParams(
                    market = marketId,
                    to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                    count = 200,
                    unit = null,
                    timeFrame = timeFrameName,
                    trades = trades.value,
                )
            }
            candleTimeFrames[4].first -> {
                CandlePagingUseCase.CandleParams(
                    market = marketId,
                    to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                    count = 200,
                    unit = null,
                    timeFrame = timeFrameName,
                    trades = trades.value,
                )
            }
            candleTimeFrames[5].first -> {
                CandlePagingUseCase.CandleParams(
                    market = marketId,
                    to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                    count = 200,
                    unit = null,
                    timeFrame = timeFrameName,
                    trades = trades.value,
                )
            }
            else -> {
                CandlePagingUseCase.CandleParams(
                    market = marketId,
                    to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                    count = 200,
                    unit = null,
                    timeFrame = timeFrameName,
                    trades = trades.value,
                )
            }
        }
    }

    private fun reqTradingData(startDate: String) {
        vmScopeJob(
            coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
                _trades.value = listOf()
            }
        ) {
            tradingDataUseCase.reqTradingData(startDate = startDate).collectLatest {
                _trades.value = it.reversed().flatMap { trade -> trade.order.trades ?: listOf() }

                _candleParams.value = CandlePagingUseCase.CandleParams(
                    market = marketId,
                    to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                    count = 200,
                    unit = null,
                    timeFrame = (candleTimeFrames[3].first),
                    trades = trades.value,
                )

            }
        }
    }
}

data class CandleChartModel(
    val ticker: Ticker?,
    val favoriteTickers: List<Ticker>,
)