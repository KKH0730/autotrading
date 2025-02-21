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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.collectLatest
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.data.network.response_model.Trade
import st.seno.autotrading.extensions.Divider
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.VerticalDivider
import st.seno.autotrading.extensions.formatRealPrice
import st.seno.autotrading.extensions.isFirstDayOfMonth
import st.seno.autotrading.extensions.isToday
import st.seno.autotrading.extensions.measuredTextHeight
import st.seno.autotrading.extensions.numberWithCommas
import st.seno.autotrading.extensions.pxToDp
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.model.CandleListType
import st.seno.autotrading.model.PriceTrend
import st.seno.autotrading.theme.B3000000
import st.seno.autotrading.theme.COLOR_80BDBBBB
import st.seno.autotrading.theme.FF374151
import st.seno.autotrading.theme.FFF8F8F8
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.main.trading_view.candleSpace
import st.seno.autotrading.ui.main.trading_view.isDaysCandle
import st.seno.autotrading.ui.main.trading_view.priceLevelIndicatorWidth

@SuppressLint("UseOfNonLambdaOffsetOverload")
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun CandleChartView(
    isAutoTradingView: Boolean,
    candleListTypes: LazyPagingItems<CandleListType>,
    candles: List<CandleListType.CandleType>,
    trades: List<Trade>,
    ticker: Ticker,
    selectedTimeFrame: String,
    firstOfDayOffsetList: List<Double>,
    firstVisibleIndex: Int,
    candleChartWidth: Double,
    candleChartHeight: Double,
    candleBodyWidth: Int,
    tradingVolumeHeight: Double,
    candleDateHeight: Int,
    tradesListHeightState: Double,
    candleLazyListState: LazyListState,
    candleRange: Pair<Double, Double>,
    tradingVolumeRange: Pair<Double, Double>,
    isBlockCandleVerticalDrag: Boolean,
    overlayInfo: Triple<Int, Int, CandleListType.CandleType?>,
    onDetectedMotionEvent: (Boolean) -> Unit,
    onChangedOverlayInfo: (Triple<Int, Int, CandleListType.CandleType?>) -> Unit,
    onClickTimeFrame: (Pair<String, Int>) -> Unit,
    onChangeFirstVisibleIndex: (Int) -> Unit,
    onDraggedCandleView: (Float) -> Unit,
    onChangedCandleRange: (Pair<Pair<Double, Double> , Pair<Double, Double>>) -> Unit,
    onDraggedDateView: (Float) -> Unit
) {

    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        Box(modifier = Modifier.fillMaxWidth()) {

            if (candleListTypes.itemCount != 0) {
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
                                candleRange = candleRange,
                                isBlockCandleVerticalDrag = isBlockCandleVerticalDrag,
                                onDetectedTab = onDetectedMotionEvent,
                                onChangedOverlayInfo = onChangedOverlayInfo,
                                onChangeFirstVisibleIndex = onChangeFirstVisibleIndex,
                                onDraggedCandleView = onDraggedCandleView,
                                onChangedCandleRange = onChangedCandleRange,
                            )
                            CandleDateView(
                                candleLazyListState = candleLazyListState,
                                candleListTypes = candleListTypes,
                                candles = candles,
                                firstOfDayOffsetList = firstOfDayOffsetList,
                                candleDateHeight = candleDateHeight,
                                candleChartWidth = candleChartWidth,
                                candleBodyWidth = candleBodyWidth,
                                candleSpace = candleSpace,
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
                                    onDetectedTab = onDetectedMotionEvent,
                                    onChangedOverlayInfo = onChangedOverlayInfo,
                                    onDraggedCandleView = onDraggedCandleView
                                )
                            }
                        }

                        TickerOverlayInfoPanel(
                            ticker = ticker,
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
    candleChartHeight: Double,
    candleChartWidth: Double,
    candleSpace: Int,
    candleBodyWidth: Int,
    candleRange: Pair<Double, Double>,
    isBlockCandleVerticalDrag: Boolean,
    onDetectedTab: (Boolean) -> Unit,
    onChangedOverlayInfo: (Triple<Int, Int, CandleListType.CandleType?>) -> Unit,
    onChangeFirstVisibleIndex: (Int) -> Unit,
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
                onChangeFirstVisibleIndex.invoke(firstVisibleItemIndex)

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
                        onDetectedTab.invoke(!isBlockCandleVerticalDrag)
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

                        val topSpace = (candleChartHeight * (maxPrice - highPrice)) / totalRange
                        val range = highPrice - lowPrice
                        val candleHeight = (candleChartHeight * range) / totalRange

                        Box {
                            CandleView(
                                candleHeight = candleHeight,
                                openingPrice = openingPrice,
                                highPrice = highPrice,
                                lowPrice = lowPrice,
                                tradePrice = tradePrice,
                                candleTailWidth = candleTailWidth,
                                candleBodyWidth = candleBodyWidth,
                                yOffset = topSpace,
                                side = candleListType.candle.side,
                                backgroundColor = FFFFFFFF
                            )
                            if (candleListType.candle.candleDateTimeUtc.isFirstDayOfMonth()) {
                                0.2.VerticalDivider(
                                    xOffset = candleChartWidth,
                                    modifier = Modifier
                                        .zIndex(zIndex = 0f)
                                        .align(alignment = Alignment.Center)
                                        .background(color = COLOR_80BDBBBB)
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
                if (i == 0) {
                    if (realOffsetX <= candleLazyListState.layoutInfo.visibleItemsInfo[i].offset) {
                        rawIndex = candleLazyListState.layoutInfo.visibleItemsInfo[0].index
                        break
                    }
                } else {
                    if (realOffsetX > candleLazyListState.layoutInfo.visibleItemsInfo[i - 1].offset && realOffsetX <= candleLazyListState.layoutInfo.visibleItemsInfo[i].offset + 4) {
                        rawIndex = candleLazyListState.layoutInfo.visibleItemsInfo[i].index
                        break
                    }

                    if (i == candleLazyListState.layoutInfo.visibleItemsInfo.lastIndex) {
                        rawIndex = lastItem.index
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

//endregion

//region(ChartUnderLayer)
@Composable
fun ChartUnderLayerView(
    chartHeight: Double,
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
        val pair = accumulatedValue to (chartHeight * (maxPrice - accumulatedValue)) / (maxPrice - minPrice)
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
            indicatorPricePairs = indicatorPricePairs,
            modifier = Modifier.weight(weight = 0.85f)
        )
        PriceLevelIndicator(
            chartHeight = chartHeight,
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
    chartHeight: Double,
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
                        .offset(
                            y = (pair.second + (pair.first
                                .toString()
                                .measuredTextHeight(fontSize = 8)
                                .pxToDp() / 2)).dp
                        )
                        .background(color = COLOR_80BDBBBB)
                )
            }
        }
    }
}

@Composable
fun PriceLevelIndicator(
    chartHeight: Double,
    maxPrice: Double,
    minPrice: Double,
    highlightPrice: Double,
    highlightPriceTrend: PriceTrend,
    indicatorPricePairs: List<Pair<Double, Double>>,
    isTradingVolume: Boolean,
) {
    val highlightPriceYOffset = (chartHeight * (maxPrice - highlightPrice)) / (maxPrice - minPrice)

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
                     .offset(y = pair.second.dp)
             )
         }

         val value = if (isTradingVolume) highlightPrice.numberWithCommas() else highlightPrice.formatRealPrice()
         Box(
             modifier = Modifier
                 .offset(
                     y = (highlightPriceYOffset - (value
                         .measuredTextHeight(fontSize = 8)
                         .pxToDp() / 2)).dp
                 )
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
    candleLazyListState: LazyListState,
    candleListTypes: LazyPagingItems<CandleListType>,
    candles: List<CandleListType.CandleType>,
    firstOfDayOffsetList: List<Double>,
    candleDateHeight: Int,
    candleChartWidth: Double,
    candleBodyWidth: Int,
    candleSpace: Int,
    onDraggedDateView: (Float) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(height = candleDateHeight.dp)
            .background(color = FFF8F8F8)
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
//        LazyRow(
//            state = candleLazyListState,
//            verticalAlignment = Alignment.CenterVertically,
//            reverseLayout = true,
//            horizontalArrangement = Arrangement.spacedBy(space = candleSpace.dp),
//            modifier = Modifier
//                .width(width = candleChartWidth.dp)
//                .height(height = candleDateHeight.dp)
//        ) {
//            items(
//                count = candles.itemCount,
//            ) { index ->
//                Box(
//                    contentAlignment = Alignment.Center,
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .zIndex(zIndex = if (candles[index]?.candleDateTimeUtc?.isFirstDayOfMonth() == true) 2f else 0f)
//                        .background(color = FFCEDBDA)
//                ) {
//                    if (candles[index]?.candleDateTimeUtc?.isFirstDayOfMonth() == true) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxHeight()
//                                .width(width = candleBodyWidth.dp)
//                                .background(color = Color.Magenta)
//                                .align(alignment = Alignment.Center)
//                        )
//                        val date = candles[index]?.candleDateTimeUtc?.parseDateFormat(
//                            inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
//                            outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"),
//                        ) ?: ""
//                        val textWidth = date.measureTextWidth(fontSizeSp = 8f).pxToDp()
//                        Text(
//                            candles[index]?.candleDateTimeUtc?.parseDateFormat(
//                                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
//                                outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"),
//                            ) ?: "",
//                            style = TextStyle(
//                                fontSize = 8.textDp,
//                                fontWeight = FontWeight.Normal,
//                                color = FF000000
//                            ),
//                            modifier = Modifier
//                                .width(width = textWidth.dp)
//                                .offset(x = firstOfDayOffsetList[index].dp)
//                                .background(Color.Green)
//                        )
//                    } else {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxHeight()
//                                .width(width = candleBodyWidth.dp)
//                                .offset(x = 0.dp)
//                        )
//                    }
//                }
//            }
//        }
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Icon(
//                painter = painterResource(R.drawable.ic_vertical_handle),
//                contentDescription = null,
//                tint = FF626975,
//                modifier = Modifier.size(height = 10.dp, width = 10.dp)
//            )
//        }
    }
}
//endregion

//region(TickerOverlay)
@Composable
fun TickerOverlayInfoPanel(
    ticker: Ticker,
    isBlockCandleVerticalDrag: Boolean,
    overlayInfoHeight: Double,
    overlayInfoWidth: Double,
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
                            Text(
                                (if (candleType.candle.candleDateTimeUtc.isToday()) ticker.tradePrice else candleType.candle.tradePrice).formatRealPrice(),
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