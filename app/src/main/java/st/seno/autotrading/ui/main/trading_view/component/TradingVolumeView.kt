package st.seno.autotrading.ui.main.trading_view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.compose.LazyPagingItems
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.extensions.FullHeightSpacer
import st.seno.autotrading.extensions.VerticalDivider
import st.seno.autotrading.extensions.isFirstDayOfMonth
import st.seno.autotrading.model.CandleListType
import st.seno.autotrading.model.PriceTrend
import st.seno.autotrading.theme.COLOR_80BDBBBB
import st.seno.autotrading.theme.FFBDBBBB
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.main.trading_view.isDaysCandle

//region(TradingVolumeView)
@Composable
fun TradingVolumeView(
    candleLazyListState: LazyListState,
    candleListTypes: LazyPagingItems<CandleListType>,
    candles: List<CandleListType.CandleType>,
    ticker: Ticker,
    selectedTimeFrame: String,
    firstVisibleIndex: Int,
    tradingVolumeHeight: Double,
    candleSpace: Int,
    candleChartHeight: Double,
    candleDateHeight: Int,
    candleChartWidth: Double,
    candleBodyWidth: Int,
    tradingVolumeRange: Pair<Double, Double>,
    isBlockCandleVerticalDrag: Boolean,
    onDetectedMotionEvent: (Boolean) -> Unit,
    onChangedOverlayInfo: (Triple<Int, Int, CandleListType.CandleType?>) -> Unit,
    onDraggedCandleView: (Float) -> Unit,
) {

    val maxVolume = tradingVolumeRange.second.takeIf { it != 0.0 } ?: candles.maxOf { it.candle.candleAccTradeVolume }
    val minVolume = tradingVolumeRange.first

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = tradingVolumeHeight.dp)
            .pointerInput(key1 = isBlockCandleVerticalDrag) {
                detectTapGestures(
                    onTap = { offset ->
                        if (!isBlockCandleVerticalDrag) {
                            onDetectedOverlayGesture(
                                candleLazyListState = candleLazyListState,
                                candleListTypes = candleListTypes,
                                offsetX = offset.x,
                                offsetY = offset.y,
                                topSpace = candleChartHeight.toInt() + candleDateHeight,
                                onChangedOverlayInfo = onChangedOverlayInfo
                            )
                        }
                        onDetectedMotionEvent.invoke(!isBlockCandleVerticalDrag)
                    }
                )
            }
    ) {
        0.2.VerticalDivider(xOffset = candleChartWidth, backgroundColor = FFBDBBBB)
        LazyRow(
            state = candleLazyListState,
            reverseLayout = true,
            userScrollEnabled = !isBlockCandleVerticalDrag,
            horizontalArrangement = Arrangement.spacedBy(space = candleSpace.dp, alignment = Alignment.End),
            modifier = Modifier
                .width(width = candleChartWidth.dp)
                .height(height = tradingVolumeHeight.dp)
                .background(color = FFFFFFFF)
                .pointerInput(key1 = isBlockCandleVerticalDrag) {
                    if (isBlockCandleVerticalDrag) {
                        detectDragGestures { change, _ ->
                            onDetectedOverlayGesture(
                                candleLazyListState = candleLazyListState,
                                candleListTypes = candleListTypes,
                                offsetX = change.position.x,
                                offsetY = change.position.y,
                                topSpace = candleChartHeight.toInt() + candleDateHeight,
                                onChangedOverlayInfo = onChangedOverlayInfo
                            )
                        }
                    } else {
                        detectVerticalDragGestures(onVerticalDrag = { _, dragAmount -> onDraggedCandleView.invoke(dragAmount) })
                    }
                }
        ) {
            items(
                count = candleListTypes.itemCount,
            ) { index ->
                when(val candleListType = candleListTypes[index]) {
                    is CandleListType.CandleType -> {
                        val volumeChartHeight = if (maxVolume < candleListType.candle.candleAccTradeVolume) {
                            0.0
                        } else {
                            (tradingVolumeHeight * candleListType.candle.candleAccTradeVolume) / maxVolume
                        }
                        val openingPrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.openingPrice else candleListType.candle.openingPrice
                        val tradePrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.tradePrice else candleListType.candle.tradePrice

                        Box {
                            Column {
                                FullHeightSpacer()
                                VolumeChartView(
                                    volumeChartHeight = volumeChartHeight,
                                    openingPrice = openingPrice,
                                    tradePrice =  tradePrice,
                                    candleBodyWidth = candleBodyWidth,
                                )
                            }
                            if (candleListType.candle.candleDateTimeUtc.isFirstDayOfMonth()) {
                                Spacer(
                                    modifier = Modifier
                                        .width(width = 0.2.dp)
                                        .fillMaxHeight()
                                        .zIndex(zIndex = 0f)
                                        .align(alignment = Alignment.Center)
                                        .background(color = COLOR_80BDBBBB)
                                )
                            }
                        }
                    }
                    is CandleListType.Blank -> { CandleBlank(width = candleListType.width, height = tradingVolumeHeight.toInt()) }
                    else -> {}
                }
            }
        }
        if (firstVisibleIndex in 0..< candleListTypes.itemCount) {
            val candleListType = if (candleListTypes[firstVisibleIndex] is CandleListType.CandleType) {
                candleListTypes[firstVisibleIndex]
            } else {
                candleListTypes.itemSnapshotList.items.firstOrNull { it is CandleListType.CandleType }
            }

            if (candleListType is CandleListType.CandleType) {
                val highlightVolume = candleListType.candle.candleAccTradeVolume
                val trendType: PriceTrend = PriceTrend.getPriceTrend(
                    targetPrice = candleListType.candle.tradePrice,
                    openingPrice = candleListType.candle.openingPrice
                )

                ChartUnderLayerView(
                    chartHeight = tradingVolumeHeight,
                    tradeBadgeSpacing = 0,
                    maxPrice = maxVolume,
                    minPrice = minVolume,
                    highlightPrice = highlightVolume,
                    highlightPriceTrend = trendType,
                    isTradingVolume = true,
                    level = 4
                )
            }
        }
    }
}
//endregion

@Composable
fun VolumeChartView(
    volumeChartHeight: Double,
    openingPrice: Double,
    tradePrice: Double,
    candleBodyWidth: Int,
) {
    val trendType: PriceTrend = PriceTrend.getPriceTrend(targetPrice = tradePrice, openingPrice = openingPrice)

    Box(
        modifier = Modifier
            .width(width = candleBodyWidth.dp)
            .height(height = volumeChartHeight.dp)
            .background(color = trendType.color)
    )
}
