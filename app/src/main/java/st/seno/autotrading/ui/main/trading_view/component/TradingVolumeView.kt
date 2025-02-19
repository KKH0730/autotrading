package st.seno.autotrading.ui.main.trading_view.component

import androidx.compose.foundation.background
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
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.compose.LazyPagingItems
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.extensions.FullHeightSpacer
import st.seno.autotrading.extensions.VerticalDivider
import st.seno.autotrading.extensions.isFirstDayOfMonth
import st.seno.autotrading.model.PriceTrend
import st.seno.autotrading.theme.COLOR_80BDBBBB
import st.seno.autotrading.theme.FFBDBBBB
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.main.trading_view.isDaysCandle

//region(TradingVolumeView)
@Composable
fun TradingVolumeView(
    candleLazyListState: LazyListState,
    candles: LazyPagingItems<Candle>,
    ticker: Ticker,
    selectedTimeFrame: String,
    firstVisibleIndex: Int,
    tradingVolumeHeight: Double,
    candleSpace: Int,
    candleChartWidth: Double,
    candleBodyWidth: Int,
    tradingVolumeRange: Pair<Double, Double>,
    onDraggedCandleView: (Float) -> Unit,
) {
    val maxVolume = tradingVolumeRange.second.takeIf { it != 0.0 } ?: candles.itemSnapshotList.items.maxOf { it.candleAccTradeVolume }
    val minVolume = tradingVolumeRange.first

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = tradingVolumeHeight.dp)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = {},
                    onVerticalDrag = { _: PointerInputChange, dragAmount: Float ->
                        onDraggedCandleView.invoke(
                            dragAmount
                        )
                    },
                    onDragEnd = {}
                )
            }
    ) {
        0.2.VerticalDivider(xOffset = candleChartWidth, backgroundColor = FFBDBBBB)
        LazyRow(
            state = candleLazyListState,
            reverseLayout = true,
            horizontalArrangement = Arrangement.spacedBy(space = candleSpace.dp, alignment = Alignment.End),
            modifier = Modifier
                .width(width = candleChartWidth.dp)
                .height(height = tradingVolumeHeight.dp)
                .background(color = FFFFFFFF)
        ) {
            items(
                count = candles.itemCount,
            ) { index ->
                candles[index]?.let { candle ->
                    val volumeChartHeight = if (maxVolume < candle.candleAccTradeVolume) {
                        0.0
                    } else {
                        (tradingVolumeHeight * candle.candleAccTradeVolume) / maxVolume
                    }
                    val openingPrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.openingPrice else candle.openingPrice
                    val tradePrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.tradePrice else candle.tradePrice

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
                        if (candle.candleDateTimeUtc.isFirstDayOfMonth()) {
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
            }
        }
        if (firstVisibleIndex != -1 && firstVisibleIndex < candles.itemCount) {
            val highlightVolume = candles[firstVisibleIndex]?.candleAccTradeVolume ?: 0.0
            val trendType: PriceTrend = PriceTrend.getPriceTrend(
                targetPrice = candles[firstVisibleIndex]?.tradePrice ?: 0.0,
                openingPrice = candles[firstVisibleIndex]?.openingPrice ?: 0.0
            )

            ChartUnderLayerView(
                chartHeight = tradingVolumeHeight,
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