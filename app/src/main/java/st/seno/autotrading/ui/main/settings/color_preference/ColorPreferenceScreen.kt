package st.seno.autotrading.ui.main.settings.color_preference

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.screenWidth
import st.seno.autotrading.model.CandleListType
import st.seno.autotrading.prefs.PrefsManager
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.common.CommonToolbar
import st.seno.autotrading.ui.main.trading_view.component.SampleCandleView

@Composable
fun ColorPreferenceScreen(
    colors: List<Triple<Long, Long, Long>>,
    onClickStyle: (Int) -> Unit,
    onClickBack: () -> Unit
) {
    val viewModel = hiltViewModel<ColorPreferenceViewModel>()
    val candles = viewModel.candles.collectAsStateWithLifecycle().value
    var candleRange by remember { mutableStateOf(0.0 to 0.0) }
    val candleChartHeight = 150
    val candleChartWidth = (screenWidth / 3)
    val candleTailWidth = 1.0
    val candleBodyWidth = 5
    val maxPrice = candleRange.second.takeIf { it != 0.0 } ?: candles.maxOf { it.highPrice }
    val minPrice = candleRange.first.takeIf { it != 0.0 } ?: candles.minOf { it.lowPrice }
    val totalRange = maxPrice - minPrice
    val candleLazyListState = rememberLazyListState()

    var selectedType by remember { mutableIntStateOf(PrefsManager.selectedColorIndex) }

    LaunchedEffect(candles, candleLazyListState,) {
        snapshotFlow { candleLazyListState.layoutInfo }
            .collectLatest { _ ->
                val firstVisibleItemIndex = candleLazyListState.firstVisibleItemIndex
                val lastVisibleItemIndex = candleLazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                if (lastVisibleItemIndex != null && lastVisibleItemIndex < candles.size) {
                    val visibleCandles = candles.slice(firstVisibleItemIndex..lastVisibleItemIndex).filterIsInstance<CandleListType.CandleType>()
                    if (visibleCandles.isNotEmpty()) {
                        candleRange = (visibleCandles.minOf { it.candle.lowPrice } to visibleCandles.maxOf { it.candle.highPrice })
                    }
                }
            }
    }

    Column {
        CommonToolbar(title = "Style Settings", onClickBack = onClickBack)
        24.HeightSpacer()
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 10.dp, alignment = Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            (0..2).forEach { num ->
                Card(
                    backgroundColor = FFFFFFFF,
                    shape = RoundedCornerShape(size = 10.dp),
                    border = BorderStroke(
                        width = if (selectedType == num) 1.dp else 0.5.dp,
                        color = if (selectedType == num) FF000000 else Color.LightGray
                    ),
                    modifier = Modifier.clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            selectedType = num
                            onClickStyle.invoke(num)
                        }
                    )
                ) {
                    Column {
                        Row(modifier = Modifier.padding(top = 12.dp, start = 16.dp)){
                            Box(
                                modifier = Modifier
                                    .size(size = 16.dp)
                                    .clip(shape = RoundedCornerShape(size = 6.dp))
                                    .background(color = Color(colors[num].first))
                            )
                            5.WidthSpacer()
                            Box(
                                modifier = Modifier
                                    .size(size = 16.dp)
                                    .clip(shape = RoundedCornerShape(size = 6.dp))
                                    .background(color = Color(colors[num].second))
                            )
                        }
                        LazyRow(
                            state = candleLazyListState,
                            reverseLayout = true,
                            userScrollEnabled = false,
                            horizontalArrangement = Arrangement.spacedBy(space = 1.dp, alignment = Alignment.End),
                            modifier = Modifier
                                .width(width = candleChartWidth.dp)
                                .height(height = candleChartHeight.dp)
                                .padding(vertical = 5.dp, horizontal = 24.dp)
                        ) {
                            items(
                                count = candles.size,
                            ) { index ->
                                val highPrice = candles[index].highPrice
                                val lowPrice = candles[index].lowPrice
                                val tradePrice = candles[index].tradePrice
                                val openingPrice = candles[index].openingPrice

                                val topSpace = (candleChartHeight * (maxPrice - highPrice)) / totalRange
                                val range = highPrice - lowPrice
                                val candleHeight = (candleChartHeight * range) / totalRange

                                SampleCandleView(
                                    candleHeight = candleHeight,
                                    openingPrice = openingPrice,
                                    highPrice = highPrice,
                                    lowPrice = lowPrice,
                                    tradePrice = tradePrice,
                                    candleTailWidth = candleTailWidth,
                                    candleBodyWidth = candleBodyWidth,
                                    yOffset = topSpace,
                                    backgroundColor = FFFFFFFF,
                                    riseColor = Color(colors[num].first),
                                    fallColor = Color(colors[num].second),
                                    evenColor = Color(colors[num].third)
                                )
                                Color(0xFFFFF508)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getRandom() = (30..55).random()
