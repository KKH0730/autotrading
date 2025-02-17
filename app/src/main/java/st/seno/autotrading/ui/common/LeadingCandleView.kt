package st.seno.autotrading.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import st.seno.autotrading.model.PriceTrend
import st.seno.autotrading.theme.FFCEDBDA
import kotlin.math.abs

/**
 * openingPrice: 시가
 * highPrice: 고가
 * lowPrice: 저기
 * tradePrice: 현재가
 *
 */
@Composable
fun LeadingCandleView(
    openingPrice: Double,
    highPrice: Double,
    lowPrice: Double,
    tradePrice: Double,
    candleWidth: Int,
    candleHeight: Int
) {
   val trendType: PriceTrend = PriceTrend.getPriceTrend(targetPrice = tradePrice, openingPrice = openingPrice)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(width = candleWidth.dp)
            .height(height = candleHeight.dp)
            .background(color = FFCEDBDA)
    ) {
        Box(
            modifier = Modifier.weight(weight = 1f)
        ) {
            CandleTail(
                trendType = trendType,
                openingPrice = openingPrice,
                highPrice = highPrice,
                candleHeight = candleHeight,
                range = highPrice - lowPrice,
                modifier = Modifier.align(alignment = Alignment.BottomCenter)
            )
            if (tradePrice > openingPrice) {
                CandleBody(
                    trendType = trendType,
                    openingPrice = openingPrice,
                    tradePrice = tradePrice,
                    candleWidth = candleWidth,
                    candleHeight = candleHeight,
                    range = highPrice - lowPrice,
                    modifier = Modifier.align(alignment = Alignment.BottomCenter)
                )
            }
        }
        if (tradePrice == openingPrice) {
            Box(
                modifier = Modifier
                    .width(width = candleWidth.dp)
                    .height(height = 2.dp)
                    .background(color = trendType.color)
            )
        }
        Box(
            modifier = Modifier.weight(weight = 1f)
        ) {
            CandleTail(
                trendType = trendType,
                openingPrice = openingPrice,
                highPrice = lowPrice,
                candleHeight = candleHeight,
                range = highPrice - lowPrice,
                modifier = Modifier.align(alignment = Alignment.TopCenter)
            )
            if (tradePrice < openingPrice) {
                CandleBody(
                    trendType = trendType,
                    openingPrice = openingPrice,
                    tradePrice = tradePrice,
                    candleWidth = candleWidth,
                    candleHeight = candleHeight,
                    range = highPrice - lowPrice,
                    modifier = Modifier.align(alignment = Alignment.BottomCenter)
                )
            }
        }
    }
}

//region(CandleTail)
@Composable
fun CandleTail(
    trendType: PriceTrend,
    openingPrice: Double,
    highPrice: Double,
    candleHeight: Int,
    range: Double,
    modifier: Modifier
) {
    val tailHeight = (((abs(highPrice - openingPrice)) / range) * candleHeight).dp
    Spacer(
        modifier = modifier
            .width(width = 1.dp)
            .height(height = tailHeight)
            .background(color = trendType.color)
    )
}
//endregion

@Composable
fun CandleBody(
    trendType: PriceTrend,
    openingPrice: Double,
    tradePrice: Double,
    candleWidth: Int,
    candleHeight: Int,
    range: Double,
    modifier: Modifier
) {
    val bodyHeight = (((abs(tradePrice - openingPrice)) / range) * candleHeight).dp
    Spacer(
        modifier = modifier
            .width(width = candleWidth.dp)
            .height(height = bodyHeight)
            .background(color = trendType.color)
    )
}

@Composable
fun LeadingCandleChartShimmer(candleWidth: Int, candleHeight: Int, shimmerBrush: Brush) {
    Box(
        modifier = Modifier
            .width(width = candleWidth.dp)
            .height(height = candleHeight.dp)
            .background(brush = shimmerBrush)
    )
}