package st.seno.autotrading.ui.main.trading_view

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.extensions.pxToDp
import st.seno.autotrading.extensions.screenHeight
import st.seno.autotrading.extensions.screenWidth
import st.seno.autotrading.extensions.update
import kotlin.math.abs

const val MAX_CANDLE_CHART_HEIGHT = 500.0
const val MIN_CANDLE_CHART_HEIGHT = 0.0
const val MAX_TRADING_VOLUME_HEIGHT = 500.0
const val MIN_TRADING_VOLUME_HEIGHT = 0.0
const val maxCandleBodyWidth = 1
const val tradingViewToolbarHeight = 40
const val tradingViewHeaderHeight = 100
const val candleTimeframeHeight = 25
const val candleDateHeight = 15
const val bottomSpacerHeight = 10

var candleChartHeight = (screenHeight.pxToDp() - tradingViewToolbarHeight - tradingViewHeaderHeight - candleTimeframeHeight) * 0.6
var volumeChartHeight = ((screenHeight.pxToDp() - tradingViewToolbarHeight - tradingViewHeaderHeight - candleTimeframeHeight) * 0.4) - ((candleDateHeight + bottomSpacerHeight) * 2)
var candleChartWidth = screenWidth.pxToDp() * 0.85
val priceLevelIndicatorWidth = screenWidth.pxToDp() * 0.15

var candleChartHeightWithAutoTrading = (screenHeight.pxToDp() - tradingViewToolbarHeight - tradingViewHeaderHeight - candleTimeframeHeight) * 0.3
var tradesListHeight = ((screenHeight.pxToDp() - tradingViewToolbarHeight - tradingViewHeaderHeight - candleTimeframeHeight) * 0.7) - ((candleDateHeight + bottomSpacerHeight) * 2)

val initialOverlayInfo = Triple(-10, -10, null)

data class TradingViewState(
    val isAutoTradingView: Boolean,
    val candleLazyListState: LazyListState,
    val candleChartWidthState: MutableDoubleState,
    val candleChartHeightState: MutableDoubleState,
    val tradingVolumeHeightState: MutableDoubleState,
    val tradesListHeightState: MutableDoubleState,
    val candleBodyWidthState: MutableIntState,
    val candleRangeState: MutableState<Pair<Double, Double>>,
    val tradingVolumeRangeState: MutableState<Pair<Double, Double>>,
    val selectedTimeFrameState: MutableState<String>,
    val firstVisibleIndexState: MutableIntState,
    val isBlockCandleVerticalDragState: MutableState<Boolean>,
    val overlayInfoState: MutableState<Triple<Int, Int, Candle?>>
) {

    fun updateCandleRange(candleRange: Pair<Double, Double>, tradingVolumeRange: Pair<Double, Double>) {
        this@TradingViewState.candleRangeState.update(value = candleRange)
        this@TradingViewState.tradingVolumeRangeState.update(tradingVolumeRange)
    }

    fun updateCandleBodyWidth(dragAmount: Float, candlesSize: Int) {
        val minCandleBodyWidth = ( candleChartWidthState.doubleValue / (if (candlesSize < 20) candlesSize else 20)).toInt()
        val changedCandleBodyWidth = (candleBodyWidthState.intValue + dragAmount.pxToDp()).toInt()
        val candleBodyWidth = if (changedCandleBodyWidth > minCandleBodyWidth) {
            minCandleBodyWidth
        } else if (changedCandleBodyWidth < maxCandleBodyWidth){
            maxCandleBodyWidth
        } else {
            changedCandleBodyWidth
        }
        candleBodyWidthState.update(value = candleBodyWidth)
    }

    fun updateHeight(dragAmount: Float) {
        if (dragAmount < 0) {
            candleChartHeightState.doubleValue = (candleChartHeightState.doubleValue - abs(dragAmount.pxToDp())).takeIf { it >= MIN_CANDLE_CHART_HEIGHT } ?: MIN_CANDLE_CHART_HEIGHT
            tradingVolumeHeightState.doubleValue = (tradingVolumeHeightState.doubleValue + abs(dragAmount.pxToDp())).takeIf { it <= MAX_TRADING_VOLUME_HEIGHT } ?: MAX_TRADING_VOLUME_HEIGHT
        } else {
            candleChartHeightState.doubleValue =  (candleChartHeightState.doubleValue + abs(dragAmount.pxToDp())).takeIf { it <= MAX_CANDLE_CHART_HEIGHT } ?: MAX_CANDLE_CHART_HEIGHT
            tradingVolumeHeightState.doubleValue = (tradingVolumeHeightState.doubleValue - abs(dragAmount.pxToDp())).takeIf { it >= MIN_TRADING_VOLUME_HEIGHT } ?: MIN_TRADING_VOLUME_HEIGHT
        }
    }
}


@Composable
fun rememberTradingViewState(
    isAutoTradingView: Boolean,
    candleLazyListState: LazyListState = rememberLazyListState(),
    candleChartWidthState: MutableDoubleState = mutableDoubleStateOf(candleChartWidth),
    candleChartHeightState: MutableDoubleState = mutableDoubleStateOf(if (isAutoTradingView) candleChartHeightWithAutoTrading else candleChartHeight),
    tradingVolumeHeightState: MutableDoubleState = mutableDoubleStateOf(volumeChartHeight),
    tradesListHeightState: MutableDoubleState = mutableDoubleStateOf(tradesListHeight),
    candleBodyWidthState: MutableIntState = mutableIntStateOf(maxCandleBodyWidth),
    candleRangeState: MutableState<Pair<Double, Double>> = mutableStateOf(0.0 to 0.0),
    tradingVolumeRangeState: MutableState<Pair<Double, Double>> = mutableStateOf(0.0 to 0.0),
    selectedTimeFrameState: MutableState<String> = mutableStateOf(getString(R.string.trading_view_time_frame_1d)),
    firstVisibleIndexState: MutableIntState = mutableIntStateOf(-1),
    isBlockCandleVerticalDragState: MutableState<Boolean> = mutableStateOf(false),
    overlayInfoState: MutableState<Triple<Int, Int, Candle?>> = mutableStateOf(initialOverlayInfo)
) = remember {
    TradingViewState(
        isAutoTradingView = isAutoTradingView,
        candleLazyListState = candleLazyListState,
        candleChartWidthState = candleChartWidthState,
        candleChartHeightState = candleChartHeightState,
        tradingVolumeHeightState = tradingVolumeHeightState,
        tradesListHeightState = tradesListHeightState,
        candleBodyWidthState = candleBodyWidthState,
        candleRangeState = candleRangeState,
        tradingVolumeRangeState = tradingVolumeRangeState,
        selectedTimeFrameState = selectedTimeFrameState,
        firstVisibleIndexState = firstVisibleIndexState,
        isBlockCandleVerticalDragState = isBlockCandleVerticalDragState,
        overlayInfoState = overlayInfoState
    )
}