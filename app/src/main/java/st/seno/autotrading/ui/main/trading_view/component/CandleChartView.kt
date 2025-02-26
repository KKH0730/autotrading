package st.seno.autotrading.ui.main.trading_view.component

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.collectLatest
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.data.network.response_model.Trade
import st.seno.autotrading.extensions.Divider
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.VerticalDivider
import st.seno.autotrading.extensions.dpToPx
import st.seno.autotrading.extensions.formatRealPrice
import st.seno.autotrading.extensions.isToday
import st.seno.autotrading.extensions.measuredTextHeight
import st.seno.autotrading.extensions.numberWithCommas
import st.seno.autotrading.extensions.parseDateFormat
import st.seno.autotrading.extensions.pxToDp
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.model.CandleListType
import st.seno.autotrading.model.PriceTrend
import st.seno.autotrading.model.Side
import st.seno.autotrading.theme.B3000000
import st.seno.autotrading.theme.COLOR_80BDBBBB
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF16A34A
import st.seno.autotrading.theme.FF374151
import st.seno.autotrading.theme.FF626975
import st.seno.autotrading.theme.FFDC2626
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.common.VerticalDashedLine
import st.seno.autotrading.ui.main.trading_view.candleSpace
import st.seno.autotrading.ui.main.trading_view.candleTimeFrames
import st.seno.autotrading.ui.main.trading_view.isDaysCandle
import st.seno.autotrading.ui.main.trading_view.priceLevelIndicatorWidth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("UseOfNonLambdaOffsetOverload")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CandleChartView(
    isAutoTradingView: Boolean,
    candleListTypes: LazyPagingItems<CandleListType>,
    candles: List<CandleListType.CandleType>,
    trades: List<Trade>,
    ticker: Ticker,
    dates: Triple<String, String, String>,
    selectedTimeFrame: String,
    firstVisibleIndex: Int,
    candleChartWidth: Float,
    candleChartHeight: Float,
    candleBodyWidth: Int,
    tradeBadgeSpacing: Int,
    tradingVolumeHeight: Float,
    candleDateHeight: Int,
    tradesListHeightState: Float,
    candleLazyListState: LazyListState,
    candleRange: Pair<Double, Double>,
    tradingVolumeRange: Pair<Double, Double>,
    isBlockCandleVerticalDrag: Boolean,
    overlayInfo: Triple<Int, Int, CandleListType.CandleType?>,
    onDetectedMotionEvent: (Boolean) -> Unit,
    onChangedOverlayInfo: (Triple<Int, Int, CandleListType.CandleType?>) -> Unit,
    onChangedDate: (Triple<String, String, String>) -> Unit,
    onClickTimeFrame: (Pair<String, Int>) -> Unit,
    onChangedFirstVisibleIndex: (Int) -> Unit,
    onDraggedCandleView: (Float) -> Unit,
    onChangedCandleRange: (Pair<Pair<Double, Double> , Pair<Double, Double>>) -> Unit,
    onDraggedDateView: (Float) -> Unit
) {

    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (candleListTypes.itemSnapshotList.firstOrNull { it is CandleListType.CandleType } != null) {
                Column {
                    TradingViewHeader(ticker = ticker)
                    CandleTimeframe(selectedTimeFrame = selectedTimeFrame, onClickTimeFrame = onClickTimeFrame)
                    Box {
                        Column {
                            TradingCandleView(
                                candleLazyListState = candleLazyListState,
                                candleListTypes = candleListTypes,
                                candles = candles,
                                ticker = ticker,
                                selectedTimeFrame = selectedTimeFrame,
                                firstVisibleIndex = firstVisibleIndex,
                                candleChartHeight = candleChartHeight,
                                candleChartWidth = candleChartWidth,
                                candleSpace = candleSpace,
                                candleBodyWidth = candleBodyWidth,
                                tradeBadgeSpacing = tradeBadgeSpacing,
                                candleRange = candleRange,
                                isBlockCandleVerticalDrag = isBlockCandleVerticalDrag,
                                onDetectedMotionEvent = onDetectedMotionEvent,
                                onChangedDate = onChangedDate,
                                onChangedOverlayInfo = onChangedOverlayInfo,
                                onChangedFirstVisibleIndex = onChangedFirstVisibleIndex,
                                onDraggedCandleView = onDraggedCandleView,
                                onChangedCandleRange = onChangedCandleRange,
                            )
                            CandleDateView(
                                dates = dates,
                                candleDateHeight = candleDateHeight,
                                candleChartWidth = candleChartWidth,
                                onDraggedDateView = onDraggedDateView
                            )
                            if (isAutoTradingView) {
                                24.HeightSpacer()
                                TradingHistoryView(tradesListHeightState = tradesListHeightState, trades = trades)
                            } else {
                                TradingVolumeView(
                                    candleLazyListState = candleLazyListState,
                                    candleListTypes = candleListTypes,
                                    candles = candles,
                                    ticker = ticker,
                                    selectedTimeFrame = selectedTimeFrame,
                                    firstVisibleIndex = firstVisibleIndex,
                                    tradingVolumeHeight = tradingVolumeHeight,
                                    candleSpace = candleSpace,
                                    candleChartHeight = candleChartHeight,
                                    candleDateHeight = candleDateHeight,
                                    candleChartWidth = candleChartWidth,
                                    candleBodyWidth = candleBodyWidth,
                                    tradingVolumeRange = tradingVolumeRange,
                                    isBlockCandleVerticalDrag = isBlockCandleVerticalDrag,
                                    onDetectedMotionEvent = onDetectedMotionEvent,
                                    onChangedOverlayInfo = onChangedOverlayInfo,
                                    onDraggedCandleView = onDraggedCandleView
                                )
                            }
                        }
                        DateOverlay(
                            candleChartWidth = candleChartWidth,
                            candleChartHeight = candleChartHeight,
                            candleDateHeight = candleDateHeight,
                            tradingVolumeHeight = tradingVolumeHeight
                        )
                        TickerOverlayInfoPanel(
                            ticker = ticker,
                            selectedTimeFrame = selectedTimeFrame,
                            isBlockCandleVerticalDrag = isBlockCandleVerticalDrag,
                            overlayInfoHeight = if (isAutoTradingView) candleChartHeight else candleChartHeight + candleDateHeight + tradingVolumeHeight,
                            overlayInfoWidth = candleChartWidth,
                            overlayInfo = overlayInfo
                        )
                    }
                }
            }
        }
    }
}

//region(TradingCandleView)
@Composable
fun TradingCandleView(
    candleLazyListState: LazyListState,
    candleListTypes: LazyPagingItems<CandleListType>,
    candles: List<CandleListType.CandleType>,
    ticker: Ticker,
    selectedTimeFrame: String,
    firstVisibleIndex: Int,
    candleChartHeight: Float,
    candleChartWidth: Float,
    candleSpace: Int,
    candleBodyWidth: Int,
    tradeBadgeSpacing: Int,
    candleRange: Pair<Double, Double>,
    isBlockCandleVerticalDrag: Boolean,
    onDetectedMotionEvent: (Boolean) -> Unit,
    onChangedDate: (Triple<String, String, String>) -> Unit,
    onChangedOverlayInfo: (Triple<Int, Int, CandleListType.CandleType?>) -> Unit,
    onChangedFirstVisibleIndex: (Int) -> Unit,
    onDraggedCandleView: (Float) -> Unit,
    onChangedCandleRange: (Pair<Pair<Double, Double> , Pair<Double, Double>>) -> Unit
) {
    val candleTailWidth = 1.0
    val maxPrice = candleRange.second.takeIf { it != 0.0 } ?: candles.maxOf { it.candle.highPrice }
    val minPrice = candleRange.first.takeIf { it != 0.0 } ?: candles.minOf { it.candle.lowPrice }
    val totalRange = maxPrice - minPrice

    LaunchedEffect(candles, candleLazyListState, candleBodyWidth) {
        snapshotFlow { candleLazyListState.layoutInfo }
            .collectLatest { _ ->
                val firstVisibleItemIndex = candleLazyListState.firstVisibleItemIndex
                onChangedFirstVisibleIndex.invoke(firstVisibleItemIndex)

                invokeCandleDate(
                    selectedTimeFrame = selectedTimeFrame,
                    candleLazyListState = candleLazyListState,
                    candleListTypes = candleListTypes,
                    leftOffsetX = (candleLazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.offset?.toFloat() ?: 0f) + (candleChartWidth / 8).dpToPx(),
                    centerOffsetX = (candleLazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.offset?.toFloat() ?: 0f) + (candleChartWidth / 2f).dpToPx(),
                    rightOffsetX = (candleLazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.offset?.toFloat() ?: 0f) - (candleChartWidth / 8).dpToPx(),
                    onChangedDate = onChangedDate
                )

                val lastVisibleItemIndex = candleLazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                if (lastVisibleItemIndex != null && lastVisibleItemIndex < candleListTypes.itemCount) {
                    val visibleCandles = candleListTypes.itemSnapshotList.items.slice(firstVisibleItemIndex..lastVisibleItemIndex).filterIsInstance<CandleListType.CandleType>()

                    onChangedCandleRange.invoke(
                        (visibleCandles.minOf { it.candle.lowPrice } to visibleCandles.maxOf { it.candle.highPrice })
                                to
                                (0.0 to visibleCandles.maxOf { it.candle.candleAccTradeVolume })
                    )
                }
            }
    }

    LaunchedEffect(selectedTimeFrame) {
        if (candleLazyListState.layoutInfo.totalItemsCount > 2) {
            candleLazyListState.scrollToItem(1)
        } else if (candleLazyListState.layoutInfo.totalItemsCount > 1) {
            candleLazyListState.scrollToItem(0)
        }
    }

    0.2.Divider(backgroundColor = COLOR_80BDBBBB)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = candleChartHeight.dp)
            .pointerInput(key1 = isBlockCandleVerticalDrag, key2 = candleBodyWidth) {
                detectTapGestures(
                    onTap = { offset ->
                        if (!isBlockCandleVerticalDrag) {
                            onDetectedOverlayGesture(
                                candleLazyListState = candleLazyListState,
                                candleListTypes = candleListTypes,
                                offsetX = offset.x,
                                offsetY = offset.y,
                                onChangedOverlayInfo = onChangedOverlayInfo
                            )
                        }
                        onDetectedMotionEvent.invoke(!isBlockCandleVerticalDrag)
                    }
                )
            }
    ) {
        0.2.VerticalDivider(xOffset = candleChartWidth, backgroundColor = COLOR_80BDBBBB)
        LazyRow(
            state = candleLazyListState,
            reverseLayout = true,
            userScrollEnabled = !isBlockCandleVerticalDrag,
            horizontalArrangement = Arrangement.spacedBy(space = candleSpace.dp, alignment = Alignment.End),
            modifier = Modifier
                .width(width = candleChartWidth.dp)
                .fillMaxHeight()
                .background(color = FFFFFFFF)
                .pointerInput(key1 = isBlockCandleVerticalDrag) {
                    if (isBlockCandleVerticalDrag) {
                        detectDragGestures { change, _ ->
                            onDetectedOverlayGesture(
                                candleLazyListState = candleLazyListState,
                                candleListTypes = candleListTypes,
                                offsetX = change.position.x,
                                offsetY = change.position.y,
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

                when (val candleListType = candleListTypes[index]) {
                    is CandleListType.CandleType -> {
                        val highPrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.highPrice else candleListType.candle.highPrice
                        val lowPrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.lowPrice else candleListType.candle.lowPrice
                        val tradePrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.tradePrice else candleListType.candle.tradePrice
                        val openingPrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.openingPrice else candleListType.candle.openingPrice

                        val topSpace = ((candleChartHeight - (2 * tradeBadgeSpacing)) * (maxPrice - highPrice)) / totalRange
                        val range = highPrice - lowPrice
                        val candleHeight = (candleChartHeight * range) / totalRange
                        Box {
                            if (candleListType.candle.side == Side.ASK.value) {
                                TradeBadge(
                                    side = Side.ASK,
                                    tradeBadgeSpacing = tradeBadgeSpacing,
                                    candleBodyWidth = candleBodyWidth,
                                    yOffset = topSpace,
                                    modifier = Modifier.align(alignment = Alignment.TopCenter)
                                )
                            }

                            CandleView(
                                candleHeight = candleHeight,
                                openingPrice = openingPrice,
                                highPrice = highPrice,
                                lowPrice = lowPrice,
                                tradePrice = tradePrice,
                                candleTailWidth = candleTailWidth,
                                candleBodyWidth = candleBodyWidth,
                                yOffset = topSpace + tradeBadgeSpacing,
                                backgroundColor = FFFFFFFF
                            )
                            if (candleListType.candle.side == Side.BID.value) {
                                TradeBadge(
                                    side = Side.BID,
                                    tradeBadgeSpacing = tradeBadgeSpacing,
                                    candleBodyWidth = candleBodyWidth,
                                    yOffset = topSpace + tradeBadgeSpacing + candleHeight,
                                    modifier = Modifier.align(alignment = Alignment.TopCenter)
                                )
                            }
                        }
                    }
                    is CandleListType.Blank -> { CandleBlank(width = candleListType.width, height = candleChartHeight.toInt()) }
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
                val highlightPrice = if (candleListType.candle.candleDateTimeUtc.isToday()) {
                    ticker.tradePrice
                } else {
                    candleListType.candle.tradePrice
                }
                val trendType: PriceTrend = PriceTrend.getPriceTrend(
                    targetPrice = candleListType.candle.tradePrice,
                    openingPrice = candleListType.candle.openingPrice
                )

                ChartUnderLayerView(
                    chartHeight = candleChartHeight,
                    tradeBadgeSpacing = tradeBadgeSpacing,
                    maxPrice = maxPrice,
                    minPrice = minPrice,
                    highlightPrice = highlightPrice,
                    highlightPriceTrend = trendType,
                    isTradingVolume = false,
                    level = 5
                )
            }
        }
    }
}

fun onDetectedOverlayGesture(
    candleLazyListState: LazyListState,
    candleListTypes: LazyPagingItems<CandleListType>,
    offsetX: Float,
    offsetY: Float,
    topSpace: Int = 0,
    onChangedOverlayInfo: (Triple<Int, Int, CandleListType.CandleType?>) -> Unit
) {
    candleLazyListState.layoutInfo.visibleItemsInfo
        .lastOrNull()
        ?.let { lastItem ->
            var rawIndex = -1
            val realOffsetX = candleLazyListState.layoutInfo.viewportEndOffset - offsetX
            for (i in 0 until candleLazyListState.layoutInfo.visibleItemsInfo.size) {
                if (candleLazyListState.layoutInfo.visibleItemsInfo.size < 2) {
                    rawIndex = candleLazyListState.layoutInfo.visibleItemsInfo[0].index
                    break
                } else {
                    if (i == candleLazyListState.layoutInfo.visibleItemsInfo.lastIndex) {
                        rawIndex = lastItem.index
                    } else {
                        if (realOffsetX < 0) {
                            rawIndex = 0
                            break
                        } else if (realOffsetX < candleLazyListState.layoutInfo.visibleItemsInfo[i + 1].offset && realOffsetX >= candleLazyListState.layoutInfo.visibleItemsInfo[i].offset) {
                            rawIndex = candleLazyListState.layoutInfo.visibleItemsInfo[i].index
                            break
                        }
                    }
                }
            }

            if (rawIndex >= 0 && rawIndex < candleListTypes.itemCount - 1) {
                val candleListType = candleListTypes[rawIndex]
                val thirdParam = if (candleListType is CandleListType.CandleType) {
                    candleListType
                } else {
                    null
                }

                onChangedOverlayInfo.invoke(
                    Triple(
                        offsetX.pxToDp(),
                        offsetY.pxToDp() + topSpace,
                        thirdParam
                    )
                )
            }
        }
}

@Composable
fun TradeBadge(
    side: Side,
    tradeBadgeSpacing: Int,
    candleBodyWidth: Int,
    yOffset: Double,
    modifier: Modifier = Modifier
) {
    val tint = if (side == Side.BID) {
        FF16A34A
    } else {
        FFDC2626
    }

    Box(
        modifier = modifier
            .width(width = candleBodyWidth.dp)
            .height(height = tradeBadgeSpacing.dp)
            .offset(y = yOffset.dp)
            .background(Color.Yellow)
    ) {
        Box(
            modifier = Modifier
                .align(alignment = if (side == Side.BID) Alignment.BottomCenter else Alignment.TopCenter)
        ) {
            Icon(
                painterResource(R.drawable.ic_label),
                contentDescription = null,
                tint = tint,
                modifier = Modifier
                    .size(size = candleBodyWidth.dp)
                    .align(alignment = Alignment.Center)
                    .rotate(degrees = if (side == Side.BID) -90f else 90f)
            )

            val textSize = when (candleBodyWidth) {
                in 3..5 -> 4
                in 6..8 -> 5
                in 9..12 -> 6
                else -> 7
            }

            Text(
                if (side == Side.BID) "B" else "S",
                style = TextStyle(
                    fontSize = textSize.textDp,
                    fontWeight = FontWeight.SemiBold,
                    color = FFFFFFFF
                ),
                modifier = Modifier.align(alignment = Alignment.Center)
            )
        }
    }
}

//endregion

//region(ChartUnderLayer)
@Composable
fun ChartUnderLayerView(
    chartHeight: Float,
    tradeBadgeSpacing: Int,
    maxPrice: Double,
    minPrice: Double,
    highlightPrice: Double,
    highlightPriceTrend: PriceTrend,
    isTradingVolume: Boolean,
    level: Int
) {
    var accumulatedValue = maxPrice
    val priceGap = maxPrice - minPrice
    val oneLevelPrice = priceGap / level

    val indicatorPricePairs: List<Pair<Double, Double>> = (0 until level).mapIndexed { index, i ->
        val pair = accumulatedValue to ((chartHeight - (2 * tradeBadgeSpacing)) * (maxPrice - accumulatedValue)) / (maxPrice - minPrice)
        accumulatedValue -= oneLevelPrice
        pair
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = chartHeight.dp)
    ) {
        ChardGridView(
            chartHeight = chartHeight,
            tradeBadgeSpacing = tradeBadgeSpacing,
            indicatorPricePairs = indicatorPricePairs,
            modifier = Modifier.weight(weight = 0.85f)
        )
        PriceLevelIndicator(
            chartHeight = chartHeight,
            tradeBadgeSpacing = tradeBadgeSpacing,
            maxPrice = maxPrice,
            minPrice = minPrice,
            highlightPrice = highlightPrice,
            highlightPriceTrend = highlightPriceTrend,
            indicatorPricePairs = indicatorPricePairs,
            isTradingVolume = isTradingVolume
        )
    }
}

@Composable
fun ChardGridView(
    chartHeight: Float,
    tradeBadgeSpacing: Int,
    indicatorPricePairs: List<Pair<Double, Double>>,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .height(height = chartHeight.dp)
    ) {
        indicatorPricePairs.forEachIndexed { index, pair ->
            if (index != 0) {
                Spacer(
                    modifier = Modifier
                        .height(height = 1.dp)
                        .fillMaxWidth()
                        .align(alignment = Alignment.TopStart)
                        .offset(y = ((pair.second + (pair.first.toString().measuredTextHeight(fontSize = 8).pxToDp() / 2)) + tradeBadgeSpacing).dp)
                        .background(color = COLOR_80BDBBBB)
                )
            }
        }
    }
}

@Composable
fun PriceLevelIndicator(
    chartHeight: Float,
    tradeBadgeSpacing: Int,
    maxPrice: Double,
    minPrice: Double,
    highlightPrice: Double,
    highlightPriceTrend: PriceTrend,
    indicatorPricePairs: List<Pair<Double, Double>>,
    isTradingVolume: Boolean,
) {
    val highlightPriceYOffset = ((chartHeight - (2 * tradeBadgeSpacing)) * (maxPrice - highlightPrice)) / (maxPrice - minPrice)

     Box(
         modifier = Modifier
             .width(width = priceLevelIndicatorWidth.dp)
             .height(height = chartHeight.dp)
     ) {
         indicatorPricePairs.forEach { pair ->
             Text(
                 if (isTradingVolume) {
                     pair.first.numberWithCommas()
                 } else {
                     if (pair.first < 1.0) highlightPrice.formatRealPrice() else pair.first.formatRealPrice()
                 },
                 style = TextStyle(
                     fontSize = 8.textDp,
                     fontWeight = FontWeight.Normal,
                     color = FF374151
                 ),
                 modifier = Modifier
                     .align(alignment = Alignment.TopStart)
                     .offset(y = (pair.second + tradeBadgeSpacing).dp)
             )
         }

         val value = if (isTradingVolume) highlightPrice.numberWithCommas() else highlightPrice.formatRealPrice()
         Box(
             modifier = Modifier
                 .offset(y = ((highlightPriceYOffset - (value.measuredTextHeight(fontSize = 8).pxToDp() / 2)) + tradeBadgeSpacing).dp)
                 .fillMaxWidth()
                 .clip(shape = RoundedCornerShape(size = 4.dp))
                 .align(alignment = Alignment.TopStart)
                 .background(color = highlightPriceTrend.color)
         ) {
             Text(
                 value,
                 style = TextStyle(
                     fontSize = 8.textDp,
                     fontWeight = FontWeight.Normal,
                     color = FFFFFFFF
                 ),
                 modifier = Modifier
                     .padding(vertical = 2.dp, horizontal = 2.dp)
                     .align(alignment = Alignment.Center)
             )
         }
     }
}
//endregion

//region(CandleDateView)
@Composable
fun CandleDateView(
    dates: Triple<String, String, String>,
    candleDateHeight: Int,
    candleChartWidth: Float,
    onDraggedDateView: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = candleDateHeight.dp)
            .background(color = COLOR_80BDBBBB)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = {},
                    onVerticalDrag = { _: PointerInputChange, dragAmount: Float ->
                        onDraggedDateView.invoke(dragAmount)
                    },
                    onDragEnd = {}
                )
            }
    ) {
        Box(
            modifier = Modifier
                .width(width = candleChartWidth.dp)
                .fillMaxHeight()
        ) {
            Text(
                dates.first,
                style = TextStyle(
                    fontSize = 8.textDp,
                    fontWeight = FontWeight.Medium,
                    color = FF000000
                ),
                modifier = Modifier
                    .align(alignment = Alignment.CenterStart)
                    .offset(x = (candleChartWidth / 20).dp)
            )

            Text(
                dates.second,
                style = TextStyle(
                    fontSize = 8.textDp,
                    fontWeight = FontWeight.Medium,
                    color = FF000000
                ),
                modifier = Modifier
                    .align(alignment = Alignment.Center)
            )

            Text(
                dates.third,
                style = TextStyle(
                    fontSize = 8.textDp,
                    fontWeight = FontWeight.Medium,
                    color = FF000000
                ),
                modifier = Modifier
                    .align(alignment = Alignment.CenterEnd)
                    .offset(x = -(candleChartWidth / 20).dp)
            )

        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_vertical_handle),
                contentDescription = null,
                tint = FF626975,
                modifier = Modifier.size(height = 10.dp, width = 10.dp)
            )
        }
    }
}

@Composable
fun DateOverlay(
    candleChartWidth: Float,
    candleChartHeight: Float,
    candleDateHeight: Int,
    tradingVolumeHeight: Float,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(width = candleChartWidth.dp)
    ) {
        Column(
            modifier = Modifier
                .align(alignment = Alignment.CenterStart)
                .offset(x = (candleChartWidth / 8).dp)
        ) {
            VerticalDashedLine(color = Color.LightGray, modifier = Modifier.height(height = candleChartHeight.dp))
            candleDateHeight.HeightSpacer()
            VerticalDashedLine(color = Color.LightGray, modifier = Modifier.height(height = tradingVolumeHeight.dp))
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(alignment = Alignment.Center)
        ) {
            VerticalDashedLine(color = Color.LightGray, modifier = Modifier.height(height = candleChartHeight.dp))
            candleDateHeight.HeightSpacer()
            VerticalDashedLine(color = Color.LightGray, modifier = Modifier.height(height = tradingVolumeHeight.dp))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(alignment = Alignment.CenterEnd)
                .offset(x = -(candleChartWidth / 8).dp)
        ) {
            VerticalDashedLine(color = Color.LightGray, modifier = Modifier.height(height = candleChartHeight.dp))
            candleDateHeight.HeightSpacer()
            VerticalDashedLine(color = Color.LightGray, modifier = Modifier.height(height = tradingVolumeHeight.dp))
        }
    }
}

fun invokeCandleDate(
    selectedTimeFrame: String,
    candleLazyListState: LazyListState,
    candleListTypes: LazyPagingItems<CandleListType>,
    leftOffsetX: Float,
    centerOffsetX: Float,
    rightOffsetX: Float,
    onChangedDate: (Triple<String, String, String>) -> Unit
) {
    candleLazyListState.layoutInfo.visibleItemsInfo
        .lastOrNull()
        ?.let { lastItem ->
            listOf(leftOffsetX, centerOffsetX, rightOffsetX).map { offsetX ->
                var rawIndex = -1
                val realOffsetX = candleLazyListState.layoutInfo.viewportEndOffset - offsetX
                for (i in 0 until candleLazyListState.layoutInfo.visibleItemsInfo.size) {
                    if (candleLazyListState.layoutInfo.visibleItemsInfo.size < 2) {
                        rawIndex = candleLazyListState.layoutInfo.visibleItemsInfo[0].index
                        break
                    } else {
                        if (i == candleLazyListState.layoutInfo.visibleItemsInfo.lastIndex) {
                            rawIndex = lastItem.index
                        } else {
                            if (realOffsetX < candleLazyListState.layoutInfo.visibleItemsInfo[i + 1].offset && realOffsetX >= candleLazyListState.layoutInfo.visibleItemsInfo[i].offset) {
                                rawIndex = candleLazyListState.layoutInfo.visibleItemsInfo[i].index
                                break
                            }
                        }
                    }
                }
                rawIndex
            }.map { rawIndex ->
                if (rawIndex >= 0 && rawIndex < candleListTypes.itemCount - 1) {
                    val candleListType = candleListTypes[rawIndex]

                    if (candleListType is CandleListType.CandleType) {
                        if (selectedTimeFrame == candleTimeFrames[0].first
                            || selectedTimeFrame == candleTimeFrames[1].first
                            || selectedTimeFrame == candleTimeFrames[2].first) {

                            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            val dateTime = LocalDateTime.parse(candleListType.candle.candleDateTimeUtc, inputFormatter)

                            val outputFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
                            val formattedTime = dateTime.format(outputFormatter)
                            val daySuffix = getDaySuffix(dateTime.dayOfMonth)

                            "$formattedTime, ${dateTime.dayOfMonth}$daySuffix"
                        } else {
                            candleListType.candle.candleDateTimeUtc.parseDateFormat(
                                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                                outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            )
                        }
                    } else {
                        ""
                    }
                } else {
                    ""
                }
            }.run {
                onChangedDate.invoke(Triple(this[0], this[1], this[2]))
            }
        }
}

fun getDaySuffix(day: Int): String {
    return when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
}
//endregion

//region(TickerOverlay)
@Composable
fun TickerOverlayInfoPanel(
    ticker: Ticker,
    selectedTimeFrame: String,
    isBlockCandleVerticalDrag: Boolean,
    overlayInfoHeight: Float,
    overlayInfoWidth: Float,
    overlayInfo: Triple<Int, Int, CandleListType.CandleType?>
) {
    if (isBlockCandleVerticalDrag) {
        Box(
            modifier = Modifier
                .height(height = overlayInfoHeight.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(width = 0.5.dp)
                    .fillMaxHeight()
                    .offset(x = overlayInfo.first.dp)
                    .background(color = B3000000)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 0.5.dp)
                    .offset(y = overlayInfo.second.dp)
                    .background(color = B3000000)
            )
            overlayInfo.third?.let{ candleType ->
                Box(
                    modifier = Modifier
                        .width(width = 120.dp)
                        .align(alignment = if (overlayInfo.first.pxToDp() < overlayInfoWidth / 2) Alignment.TopStart else Alignment.TopEnd)
                        .offset(x = if (overlayInfo.first.pxToDp() < overlayInfoWidth / 2) 5.dp else (-priceLevelIndicatorWidth - 5).dp)
                        .background(color = B3000000)
                ){
                    Column(
                        verticalArrangement = Arrangement.spacedBy(space = 3.dp),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp)

                    ) {
                        Text(
                            candleType.candle.candleDateTimeUtc,
                            style = TextStyle(
                                fontSize = 9.textDp,
                                fontWeight = FontWeight.SemiBold,
                                color = FFFFFFFF
                            )
                        )
                        Row {
                            Text(
                                stringResource(R.string.trading_view_overlay_opening_price),
                                style = TextStyle(
                                    fontSize = 8.textDp,
                                    fontWeight = FontWeight.Normal,
                                    color = FFFFFFFF
                                ),
                                modifier = Modifier.weight(weight = 0.5f)
                            )
                            Text(
                                candleType.candle.openingPrice.formatRealPrice(),
                                style = TextStyle(
                                    fontSize = 8.textDp,
                                    fontWeight = FontWeight.Normal,
                                    color = FFFFFFFF
                                ),
                                modifier = Modifier.weight(weight = 0.5f)
                            )
                        }
                        Row {
                            Text(
                                stringResource(R.string.trading_view_overlay_high_price),
                                style = TextStyle(
                                    fontSize = 8.textDp,
                                    fontWeight = FontWeight.Normal,
                                    color = PriceTrend.RISE.color
                                ),
                                modifier = Modifier.weight(weight = 0.5f)
                            )
                            Text(
                                candleType.candle.highPrice.formatRealPrice(),
                                style = TextStyle(
                                    fontSize = 8.textDp,
                                    fontWeight = FontWeight.Normal,
                                    color = PriceTrend.RISE.color
                                ),
                                modifier = Modifier.weight(weight = 0.5f)
                            )
                        }
                        Row {
                            Text(
                                stringResource(R.string.trading_view_overlay_low_price),
                                style = TextStyle(
                                    fontSize = 8.textDp,
                                    fontWeight = FontWeight.Normal,
                                    color = PriceTrend.FALL.color
                                ),
                                modifier = Modifier.weight(weight = 0.5f)
                            )
                            Text(
                                candleType.candle.lowPrice.formatRealPrice(),
                                style = TextStyle(
                                    fontSize = 8.textDp,
                                    fontWeight = FontWeight.Normal,
                                    color = PriceTrend.FALL.color
                                ),
                                modifier = Modifier.weight(weight = 0.5f)
                            )
                        }
                        Row {
                            Text(
                                stringResource(R.string.trading_view_overlay_trade_price),
                                style = TextStyle(
                                    fontSize = 8.textDp,
                                    fontWeight = FontWeight.Normal,
                                    color = FFFFFFFF
                                ),
                                modifier = Modifier.weight(weight = 0.5f)
                            )
                            Text((if (candleType.candle.candleDateTimeUtc.isToday() && selectedTimeFrame.isDaysCandle()) {
                                ticker.tradePrice
                            } else {
                                candleType.candle.tradePrice
                            }).formatRealPrice(),
                                style = TextStyle(
                                    fontSize = 8.textDp,
                                    fontWeight = FontWeight.Normal,
                                    color = FFFFFFFF
                                ),
                                modifier = Modifier.weight(weight = 0.5f)
                            )
                        }
                        Row {
                            Text(
                                stringResource(R.string.trading_view_overlay_volume),
                                style = TextStyle(
                                    fontSize = 8.textDp,
                                    fontWeight = FontWeight.Normal,
                                    color = FFFFFFFF
                                ),
                                modifier = Modifier.weight(weight = 0.5f)
                            )
                            Text(
                                candleType.candle.candleAccTradeVolume.numberWithCommas(),
                                style = TextStyle(
                                    fontSize = 8.textDp,
                                    fontWeight = FontWeight.Normal,
                                    color = FFFFFFFF
                                ),
                                modifier = Modifier.weight(weight = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}
//endregion