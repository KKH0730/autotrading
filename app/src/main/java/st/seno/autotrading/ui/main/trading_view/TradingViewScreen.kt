package st.seno.autotrading.ui.main.trading_view

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import st.seno.autotrading.extensions.changeBookmarkStatus
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.extensions.update
import st.seno.autotrading.model.CandleListType
import st.seno.autotrading.theme.FF2563EB
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.main.trading_view.component.CandleChartView
import st.seno.autotrading.ui.main.trading_view.component.TradingViewToolbar
import st.seno.autotrading.util.BookmarkUtil

@SuppressLint("UnrememberedMutableState")
@Composable
fun TradingViewScreen(
    tickerCode: String,
    isAutoTradingView: Boolean = false,
    onClickBack: () -> Unit
) {
    val tradingViewState = rememberTradingViewState(isAutoTradingView = isAutoTradingView)
    val tradingViewViewModel = hiltViewModel<TradingViewViewModel>()
    val candleChartModel = tradingViewViewModel.candleChartModel.collectAsStateWithLifecycle().value
    val candleListTypes = tradingViewViewModel.rawCandles.collectAsLazyPagingItems()
    val candles = candleListTypes.itemSnapshotList.items.filterIsInstance<CandleListType.CandleType>()
    val trades = tradingViewViewModel.trades.collectAsStateWithLifecycle().value
    val ticker = candleChartModel?.ticker
    val bookmarkedTickers = candleChartModel?.favoriteTickers

    Box {
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
                    isAutoTradingView = isAutoTradingView,
                    candleListTypes = candleListTypes,
                    candles = candles,
                    trades = trades,
                    ticker = ticker,
                    dates = tradingViewState.datesState.value,
                    selectedTimeFrame = tradingViewState.selectedTimeFrameState.value,
                    firstVisibleIndex = tradingViewState.firstVisibleIndexState.intValue,
                    candleChartWidth = tradingViewState.candleChartWidthState.doubleValue,
                    candleChartHeight = tradingViewState.candleChartHeightState.doubleValue,
                    candleBodyWidth = tradingViewState.candleBodyWidthState.intValue,
                    tradeBadgeSpacing = tradingViewState.tradeBadgeSpacingState.intValue,
                    tradingVolumeHeight = tradingViewState.tradingVolumeHeightState.doubleValue,
                    candleDateHeight = candleDateHeight,
                    tradesListHeightState = tradingViewState.tradesListHeightState.doubleValue,
                    candleLazyListState = tradingViewState.candleLazyListState,
                    candleRange = tradingViewState.candleRangeState.value,
                    tradingVolumeRange = tradingViewState.tradingVolumeRangeState.value,
                    isBlockCandleVerticalDrag = tradingViewState.isBlockCandleVerticalDragState.value,
                    overlayInfo = tradingViewState.overlayInfoState.value,
                    onDetectedMotionEvent = { tradingViewState.isBlockCandleVerticalDragState.update(value = it) },
                    onChangedDate = { tradingViewState.datesState.update(value = it) },
                    onChangedOverlayInfo = { tradingViewState.overlayInfoState.update(value = it) },
                    onClickTimeFrame = {
                        tradingViewState.selectedTimeFrameState.update(value = it.first)
                        tradingViewViewModel.reqCandles(timeFrameName = it.first, unit = it.second)
                    },
                    onChangedFirstVisibleIndex = { tradingViewState.firstVisibleIndexState.update(value = it) },
                    onDraggedCandleView = { tradingViewState.updateCandleBodyWidth(dragAmount = it, candlesSize = candles.size) },
                    onChangedCandleRange = { tradingViewState.updateCandleRange(candleRange = it.first, tradingVolumeRange = it.second) },
                    onDraggedDateView = { tradingViewState.updateHeight(dragAmount = it) }
                )
            } else {

            }
        }


        if (candleListTypes.loadState.refresh == LoadState.Loading) {
            CircularProgressIndicator(
                color = FF2563EB,
                strokeWidth = 4.dp,
                modifier = Modifier.align(alignment = Alignment.Center)
            )

            LaunchedEffect(candleListTypes.loadState.refresh) {
                with(tradingViewState.candleLazyListState) {
                    stopScroll()
                    scrollToItem(0)
                }
            }
        }
    }
}