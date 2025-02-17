package st.seno.autotrading.ui.main.trading_view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import st.seno.autotrading.extensions.changeBookmarkStatus
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.extensions.update
import st.seno.autotrading.ui.main.trading_view.component.CandleChartView
import st.seno.autotrading.ui.main.trading_view.component.TradingViewToolbar
import st.seno.autotrading.util.BookmarkUtil

@Composable
fun TradingViewScreen(
    tickerCode: String,
    onClickBack: () -> Unit
) {
    val tradingViewState = rememberTradingViewState()
    val tradingViewViewModel = hiltViewModel<TradingViewViewModel>()
    val candleChartModel = tradingViewViewModel.candleChartModel.collectAsStateWithLifecycle().value
    val ticker = candleChartModel?.ticker
    val bookmarkedTickers = candleChartModel?.favoriteTickers

    Column(modifier = Modifier.fillMaxSize()) {
        if (tickerCode.split("-").size == 2) {
            TradingViewToolbar(
                title =  "${tickerCode.split("-")[1]}/${tickerCode.split("-")[0]}",
                tickerCode = tickerCode,
                isAlreadyBookmarked = bookmarkedTickers?.firstOrNull{ it.code == tickerCode } != null,
                onClickBookmark = {
                    gson.changeBookmarkStatus(tickerCode = it)
                    BookmarkUtil.editBookmarkedTickers(tickerCode = it)
                },
                onClickBack = onClickBack
            )
        }
        if (candleChartModel != null && ticker != null) {
            CandleChartView(
                candles = candleChartModel.candles,
                ticker = ticker,
                selectedTimeFrame = tradingViewState.selectedTimeFrameState.value,
                firstOfDayOffsetList = candleChartModel.firstOfDayOffsetList,
                firstVisibleIndex = tradingViewState.firstVisibleIndexState.intValue,
                candleChartWidth = tradingViewState.candleChartWidthState.doubleValue,
                candleChartHeight = tradingViewState.candleChartHeightState.doubleValue,
                candleBodyWidth = tradingViewState.candleBodyWidthState.intValue,
                tradingVolumeHeight = tradingViewState.tradingVolumeHeight.doubleValue,
                candleLazyListState = tradingViewState.candleLazyListState,
                candleRange = tradingViewState.candleRangeState.value,
                tradingVolumeRange = tradingViewState.tradingVolumeRangeState.value,
                isBlockCandleVerticalDrag = tradingViewState.isBlockCandleVerticalDragState.value,
                overlayInfo = tradingViewState.overlayInfoState.value,
                onDetectedMotionEvent = { tradingViewState.isBlockCandleVerticalDragState.update(value = it) },
                onChangedOverlayInfo = { tradingViewState.overlayInfoState.update(value = it) },
                onClickTimeFrame = {
                    tradingViewState.selectedTimeFrameState.update(value = it.first)
                    tradingViewViewModel.reqCandles(timeFrameName = it.first, unit = it.second)
                 },
                onChangeFirstVisibleIndex = { tradingViewState.firstVisibleIndexState.update(value = it) },
                onDraggedCandleView = { tradingViewState.updateCandleBodyWidth(dragAmount = it, candlesSize = candleChartModel.candles.size) },
                onChangedCandleRange = { tradingViewState.updateCandleRange(candleRange = it.first, tradingVolumeRange = it.second) },
                onDraggedDateView = { tradingViewState.updateHeight(dragAmount = it) }
            )
        } else {

        }
    }
}