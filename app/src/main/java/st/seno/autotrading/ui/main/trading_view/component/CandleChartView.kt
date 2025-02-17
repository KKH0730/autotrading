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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.flow.collectLatest
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.extensions.Divider
import st.seno.autotrading.extensions.FullHeightSpacer
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.VerticalDivider
import st.seno.autotrading.extensions.dpToPx
import st.seno.autotrading.extensions.formatRealPrice
import st.seno.autotrading.extensions.isFirstDayOfMonth
import st.seno.autotrading.extensions.isToday
import st.seno.autotrading.extensions.measureTextWidth
import st.seno.autotrading.extensions.measuredTextHeight
import st.seno.autotrading.extensions.numberWithCommas
import st.seno.autotrading.extensions.parseDateFormat
import st.seno.autotrading.extensions.pxToDp
import st.seno.autotrading.extensions.screenWidth
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.model.PriceTrend
import st.seno.autotrading.theme.B3000000
import st.seno.autotrading.theme.COLOR_80BDBBBB
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF374151
import st.seno.autotrading.theme.FF626975
import st.seno.autotrading.theme.FFBDBBBB
import st.seno.autotrading.theme.FFCEDBDA
import st.seno.autotrading.theme.FFF8F8F8
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.main.trading_view.candleSpace
import st.seno.autotrading.ui.main.trading_view.isDaysCandle
import st.seno.autotrading.ui.main.trading_view.priceLevelIndicatorWidth
import timber.log.Timber
import java.time.format.DateTimeFormatter
import java.util.logging.SimpleFormatter

@SuppressLint("UseOfNonLambdaOffsetOverload")
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun CandleChartView(
    candles: List<Candle>,
    ticker: Ticker,
    selectedTimeFrame: String,
    firstOfDayOffsetList: List<Double>,
    firstVisibleIndex: Int,
    candleChartWidth: Double,
    candleChartHeight: Double,
    candleBodyWidth: Int,
    tradingVolumeHeight: Double,
    candleLazyListState: LazyListState,
    candleRange: Pair<Double, Double>,
    tradingVolumeRange: Pair<Double, Double>,
    isBlockCandleVerticalDrag: Boolean,
    overlayInfo: Triple<Int, Int, Candle?>,
    onDetectedMotionEvent: (Boolean) -> Unit,
    onChangedOverlayInfo: (Triple<Int, Int, Candle?>) -> Unit,
    onClickTimeFrame: (Pair<String, Int>) -> Unit,
    onChangeFirstVisibleIndex: (Int) -> Unit,
    onDraggedCandleView: (Float) -> Unit,
    onChangedCandleRange: (Pair<Pair<Double, Double> , Pair<Double, Double>>) -> Unit,
    onDraggedDateView: (Float) -> Unit
) {

    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (candles.isNotEmpty()) {
                Column {
                    TradingViewHeader(ticker = ticker)
                    CandleTimeframe(selectedTimeFrame = selectedTimeFrame, onClickTimeFrame = onClickTimeFrame)
                    Box {
                        TradingCandleView(
                            candleLazyListState = candleLazyListState,
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
                            onDetectedMotionEvent = onDetectedMotionEvent,
                            onChangedOverlayInfo = onChangedOverlayInfo,
                            onChangeFirstVisibleIndex = onChangeFirstVisibleIndex,
                            onDraggedCandleView = onDraggedCandleView,
                            onChangedCandleRange = onChangedCandleRange,
                        )
                        TickerOverlayInfoPanel(
                            ticker = ticker,
                            isBlockCandleVerticalDrag = isBlockCandleVerticalDrag,
                            candleChartHeight = candleChartHeight,
                            candleChartWidth = candleChartWidth,
                            overlayInfo = overlayInfo
                        )
                    }
//                    CandleDateView(
//                        candleLazyListState = candleLazyListState,
//                        candles = candles,
//                        firstOfDayOffsetList = firstOfDayOffsetList,
//                        candleDateHeight = candleDateHeight,
//                        candleChartWidth = candleChartWidth,
//                        candleBodyWidth = candleBodyWidth,
//                        candleSpace = candleSpace,
//                        onDraggedDateView = onDraggedDateView
//                    )
                    TradingVolumeView(
                        candleLazyListState = candleLazyListState,
                        candles = candles,
                        ticker = ticker,
                        selectedTimeFrame = selectedTimeFrame,
                        firstVisibleIndex = firstVisibleIndex,
                        tradingVolumeHeight = tradingVolumeHeight,
                        candleSpace = candleSpace,
                        candleChartWidth = candleChartWidth,
                        candleBodyWidth = candleBodyWidth,
                        tradingVolumeRange = tradingVolumeRange,
                        onDraggedCandleView = onDraggedCandleView
                    )
                }
            }
        }
    }
}

//region(TradingCandleView)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TradingCandleView(
    candleLazyListState: LazyListState,
    candles: List<Candle>,
    ticker: Ticker,
    selectedTimeFrame: String,
    firstVisibleIndex: Int,
    candleChartHeight: Double,
    candleChartWidth: Double,
    candleSpace: Int,
    candleBodyWidth: Int,
    candleRange: Pair<Double, Double>,
    isBlockCandleVerticalDrag: Boolean,
    onDetectedMotionEvent: (Boolean) -> Unit,
    onChangedOverlayInfo: (Triple<Int, Int, Candle?>) -> Unit,
    onChangeFirstVisibleIndex: (Int) -> Unit,
    onDraggedCandleView: (Float) -> Unit,
    onChangedCandleRange: (Pair<Pair<Double, Double> , Pair<Double, Double>>) -> Unit
) {
    val candleTailWidth = 1.0
    val maxPrice = candleRange.second.takeIf { it != 0.0 } ?: candles.maxOf { it.highPrice }
    val minPrice = candleRange.first.takeIf { it != 0.0 } ?: candles.minOf { it.lowPrice }
    val totalRange = maxPrice - minPrice

    LaunchedEffect(candles, candleLazyListState, candleBodyWidth) {
        snapshotFlow { candleLazyListState.firstVisibleItemIndex }
            .collectLatest { firstVisibleIndex ->
                onChangeFirstVisibleIndex.invoke(firstVisibleIndex)

                val lastVisibleIndex = candleLazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index

                if (lastVisibleIndex != null && lastVisibleIndex < candles.size) {
                    val visibleCandles = candles.slice(firstVisibleIndex..lastVisibleIndex)

                    onChangedCandleRange.invoke(
                        (visibleCandles.minOf { it.lowPrice } to visibleCandles.maxOf { it.highPrice })
                                to
                        (0.0 to visibleCandles.maxOf { it.candleAccTradeVolume })
                    )
                }
            }
    }

    LaunchedEffect(selectedTimeFrame) {
        candleLazyListState.scrollToItem(0)
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
                            candleLazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.let { firstItem ->
                                candleLazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.let { lastItem ->
                                    Timber.e("offset: ${offset.x}, size : ${candleLazyListState.layoutInfo.visibleItemsInfo.size}")

                                    var itemIndex = -1
                                    for (i in 0 until candleLazyListState.layoutInfo.visibleItemsInfo.size) {
                                        if (i == 0) {
                                            if (offset.x <= candleLazyListState.layoutInfo.visibleItemsInfo[i].offset) {
                                                itemIndex = 0
                                                break
                                            }
                                        } else {
                                            val prevGap =  (candleLazyListState.layoutInfo.visibleItemsInfo[i].offset - candleLazyListState.layoutInfo.visibleItemsInfo[i - 1].offset) / 2
                                            val nextGap = if (i == candleLazyListState.layoutInfo.visibleItemsInfo.lastIndex) {
                                                0
                                            } else {
                                                (candleLazyListState.layoutInfo.visibleItemsInfo[i + 1].offset - candleLazyListState.layoutInfo.visibleItemsInfo[i].offset) / 2
                                            }
//                                            if (offset.x > candleLazyListState.layoutInfo.visibleItemsInfo[i - 1].offset && offset.x <= candleLazyListState.layoutInfo.visibleItemsInfo[i].offset + 4) {
//                                                itemIndex = i
//                                                break
//                                            }

                                            if (offset.x.toInt() > (candleLazyListState.layoutInfo.visibleItemsInfo[i - 1].offset) && offset.x.toInt()<= candleLazyListState.layoutInfo.visibleItemsInfo[i].offset + nextGap) {
                                                itemIndex = i
                                                break
                                            }

                                            if (i == candleLazyListState.layoutInfo.visibleItemsInfo.lastIndex) {
                                                itemIndex = candleLazyListState.layoutInfo.visibleItemsInfo.lastIndex
                                            }
                                        }
                                    }


                                    val rawIndex = lastItem.index - itemIndex
                                    Timber.e("itemIndex : $itemIndex, rawIndex : $rawIndex")
                                    Timber.e("                     ")
                                    if (rawIndex >= 0 && rawIndex < candles.lastIndex) {
                                        onChangedOverlayInfo.invoke(
                                            Triple(
                                                offset.x.pxToDp(),
                                                offset.y.pxToDp(),
                                                candles[rawIndex]
                                            )
                                        )
                                    }
                                }
                            }
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
                            candleLazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.let { firstItem ->
                                candleLazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.let { lastItem ->
                                    var itemIndex = -1
                                    for (i in 0 until candleLazyListState.layoutInfo.visibleItemsInfo.size) {
                                        if (i == 0) {
                                            if (change.position.x <= candleLazyListState.layoutInfo.visibleItemsInfo[i].offset) {
                                                itemIndex = 0
                                                break
                                            }
                                        } else {
                                            if (change.position.x > candleLazyListState.layoutInfo.visibleItemsInfo[i - 1].offset && change.position.x <= candleLazyListState.layoutInfo.visibleItemsInfo[i].offset + 4) {
                                                itemIndex = i
                                                break
                                            }

                                            if (i == candleLazyListState.layoutInfo.visibleItemsInfo.lastIndex) {
                                                itemIndex = candleLazyListState.layoutInfo.visibleItemsInfo.lastIndex
                                            }
                                        }
                                    }

                                    val rawIndex = lastItem.index - itemIndex
                                    if (rawIndex >= 0 && rawIndex < candles.lastIndex) {
                                        onChangedOverlayInfo.invoke(
                                            Triple(
                                                change.position.x.pxToDp(),
                                                change.position.y.pxToDp(),
                                                candles[rawIndex]
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        detectVerticalDragGestures(
                            onVerticalDrag = { p: PointerInputChange, dragAmount: Float ->
                                onDraggedCandleView.invoke(dragAmount)
                            },
                        )
                    }
                }
        ) {
            items(
                count = candles.size,
                key = { index -> candles[index].candleDateTimeUtc },
            ) { index ->
                val highPrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.highPrice else candles[index].highPrice
                val lowPrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.lowPrice else candles[index].lowPrice
                val tradePrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.tradePrice else candles[index].tradePrice
                val openingPrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.openingPrice else candles[index].openingPrice

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
                        backgroundColor = FFFFFFFF
                    )
                    if (candles[index].candleDateTimeUtc.isFirstDayOfMonth()) {
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
        }

        if (firstVisibleIndex != -1 && firstVisibleIndex < candles.size) {
            val highlightPrice = if (candles[firstVisibleIndex].candleDateTimeUtc.isToday()) {
                ticker.tradePrice
            } else {
                candles[firstVisibleIndex].tradePrice
            }
            val trendType: PriceTrend = PriceTrend.getPriceTrend(targetPrice = candles[firstVisibleIndex].tradePrice, openingPrice = candles[firstVisibleIndex].openingPrice)

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
    candles: List<Candle>,
    firstOfDayOffsetList: List<Double>,
    candleDateHeight: Int,
    candleChartWidth: Double,
    candleBodyWidth: Double,
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
        LazyRow(
            state = candleLazyListState,
            verticalAlignment = Alignment.CenterVertically,
            reverseLayout = true,
            horizontalArrangement = Arrangement.spacedBy(space = candleSpace.dp),
            modifier = Modifier
                .width(width = candleChartWidth.dp)
                .height(height = candleDateHeight.dp)
        ) {
            items(
                count = candles.size,
            ) { index ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .zIndex(zIndex = if (candles[index].candleDateTimeUtc.isFirstDayOfMonth()) 2f else 0f)
                        .background(color = FFCEDBDA)
                ) {
                    if (candles[index].candleDateTimeUtc.isFirstDayOfMonth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width = candleBodyWidth.dp)
                                .background(color = Color.Magenta)
                                .align(alignment = Alignment.Center)
                        )
                        val date = candles[index].candleDateTimeUtc.parseDateFormat(
                            inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                            outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                        )
                        val textWidth = date.measureTextWidth(fontSizeSp = 8f).pxToDp()
                        Text(
                            candles[index].candleDateTimeUtc.parseDateFormat(
                                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                                outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                            ),
                            style = TextStyle(
                                fontSize = 8.textDp,
                                fontWeight = FontWeight.Normal,
                                color = FF000000
                            ),
                            modifier = Modifier
                                .width(width = textWidth.dp)
                                .offset(x = firstOfDayOffsetList[index].dp)
                                .background(Color.Green)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width = candleBodyWidth.dp)
                                .offset(x = 0.dp)
                        )
                    }
                }
            }
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
//endregion

//region(TradingVolumeView)
@Composable
fun TradingVolumeView(
    candleLazyListState: LazyListState,
    candles: List<Candle>,
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
    val maxVolume = tradingVolumeRange.second.takeIf { it != 0.0 } ?: candles.maxOf { it.candleAccTradeVolume }
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
                count = candles.size,
                key = { index -> candles[index].candleDateTimeUtc },
            ) { index ->
                val volumeChartHeight = if (maxVolume < candles[index].candleAccTradeVolume) {
                    0.0
                } else {
                    (tradingVolumeHeight * candles[index].candleAccTradeVolume) / maxVolume
                }
                val openingPrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.openingPrice else candles[index].openingPrice
                val tradePrice = if (index == 0 && selectedTimeFrame.isDaysCandle()) ticker.tradePrice else candles[index].tradePrice

                Box {
                    Column {
                        FullHeightSpacer()
                        VolumeCandleView(
                            volumeChartHeight = volumeChartHeight,
                            openingPrice = openingPrice,
                            tradePrice =  tradePrice,
                            candleBodyWidth = candleBodyWidth,
                        )
                    }
                    if (candles[index].candleDateTimeUtc.isFirstDayOfMonth()) {
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
        if (firstVisibleIndex != -1 && firstVisibleIndex < candles.size) {
            val highlightVolume = candles[firstVisibleIndex].candleAccTradeVolume
            val trendType: PriceTrend = PriceTrend.getPriceTrend(targetPrice = candles[firstVisibleIndex].tradePrice, openingPrice = candles[firstVisibleIndex].openingPrice)

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

//region(TickerOverlay)
@Composable
fun TickerOverlayInfoPanel(
    ticker: Ticker,
    isBlockCandleVerticalDrag: Boolean,
    candleChartHeight: Double,
    candleChartWidth: Double,
    overlayInfo: Triple<Int, Int, Candle?>
) {
    if (isBlockCandleVerticalDrag) {
        Box(
            modifier = Modifier
                .height(height = candleChartHeight.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(width = 1.dp)
                    .fillMaxHeight()
                    .offset(x = overlayInfo.first.dp)
                    .background(color = B3000000)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 1.dp)
                    .offset(y = overlayInfo.second.dp)
                    .background(color = B3000000)
            )
            overlayInfo.third?.let{ candle ->
                Box(
                    modifier = Modifier
                        .width(width = 120.dp)
                        .align(alignment = if (overlayInfo.first.pxToDp() < candleChartWidth / 2) Alignment.TopStart else Alignment.TopEnd)
                        .offset(x = if (overlayInfo.first.pxToDp() < candleChartWidth / 2) 5.dp else (-priceLevelIndicatorWidth - 5).dp)
                        .background(color = B3000000)
                ){
                    Column(
                        verticalArrangement = Arrangement.spacedBy(space = 3.dp),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp)

                    ) {
                        Text(
                            candle.candleDateTimeUtc.parseDateFormat(
                                inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                                outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                            ),
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
                                candle.openingPrice.formatRealPrice(),
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
                                candle.highPrice.formatRealPrice(),
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
                                candle.lowPrice.formatRealPrice(),
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
                                ticker.tradePrice.formatRealPrice(),
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
                                candle.candleAccTradeVolume.numberWithCommas(),
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