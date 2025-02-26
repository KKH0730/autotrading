package st.seno.autotrading.ui.main.trading_view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import st.seno.autotrading.extensions.parseDateFormat
import st.seno.autotrading.model.PriceTrend
import timber.log.Timber
import java.time.format.DateTimeFormatter
import kotlin.math.abs

/**
 * openingPrice: 시가
 * highPrice: 고가
 * lowPrice: 저기
 * tradePrice: 현재가
 *
 */
@Composable
fun CandleView(
    candleHeight: Double,
    openingPrice: Double,
    highPrice: Double,
    lowPrice: Double,
    tradePrice: Double,
    candleTailWidth: Double,
    candleBodyWidth: Int,
    yOffset: Double,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {

    val trendType: PriceTrend = PriceTrend.getPriceTrend(targetPrice = tradePrice, openingPrice = openingPrice)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(width = candleBodyWidth.dp)
            .height(height = candleHeight.dp)
            .offset(y = yOffset.dp)
            .background(color = backgroundColor)

    ) {
        val candleTopTailHeight = (((highPrice - openingPrice) * candleHeight) / abs(highPrice - lowPrice))
        val candleBottomTailHeight = (((openingPrice - lowPrice) * candleHeight) / abs(highPrice - lowPrice))
        val candleBodyHeight = (((abs(tradePrice - openingPrice)) * candleHeight) / abs(highPrice - lowPrice))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = candleTopTailHeight.dp)
        ) {
            CandleTail(
                candleTailWidth = candleTailWidth,
                candleTailHeight = candleTopTailHeight,
                color = trendType.color,
                modifier = Modifier.align(alignment = Alignment.BottomCenter)
            )
            if (tradePrice > openingPrice) {
                CandleBody(
                    candleBodyWidth = candleBodyWidth,
                    candleBodyHeight = candleBodyHeight,
                    color = trendType.color,
                    modifier = Modifier.align(alignment = Alignment.BottomCenter)
                )
            }
        }
        if (tradePrice == openingPrice) {
            Box(
                modifier = Modifier
                    .width(width = candleBodyWidth.dp)
                    .height(height = 2.dp)
                    .background(color = trendType.color)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = candleBottomTailHeight.dp)
        ) {
            CandleTail(
                candleTailWidth = candleTailWidth,
                candleTailHeight = candleBottomTailHeight,
                color = trendType.color,
                modifier = Modifier.align(alignment = Alignment.TopCenter)
            )
            if (tradePrice < openingPrice) {
                CandleBody(
                    candleBodyWidth = candleBodyWidth,
                    candleBodyHeight = candleBodyHeight,
                    color = trendType.color,
                    modifier = Modifier.align(alignment = Alignment.TopCenter)
                )
            }
        }
    }
}

//region(CandleTail)
@Composable
fun CandleTail(
    candleTailWidth: Double,
    candleTailHeight: Double,
    color: Color,
    modifier: Modifier
) {
    Spacer(
        modifier = modifier
            .width(width = candleTailWidth.dp)
            .height(height = candleTailHeight.dp)
            .background(color = color)
    )
}
//endregion

//region(CandleBody)
@Composable
fun CandleBody(
    candleBodyWidth: Int,
    candleBodyHeight: Double,
    color: Color,
    modifier: Modifier
) {
    Spacer(
        modifier = modifier
            .width(width = candleBodyWidth.dp)
            .height(height = candleBodyHeight.dp)
            .background(color = color)
    )
}
//endregion

@Composable
fun SampleCandleView(
    candleHeight: Double,
    openingPrice: Double,
    highPrice: Double,
    lowPrice: Double,
    tradePrice: Double,
    candleTailWidth: Double,
    candleBodyWidth: Int,
    yOffset: Double,
    backgroundColor: Color,
    riseColor: Color,
    fallColor: Color,
    evenColor: Color,
    modifier: Modifier = Modifier
) {

    val trendType: PriceTrend = PriceTrend.getPriceTrend(targetPrice = tradePrice, openingPrice = openingPrice)
    val color = when(trendType) {
        PriceTrend.RISE -> riseColor
        PriceTrend.FALL -> fallColor
        else -> evenColor
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(width = candleBodyWidth.dp)
            .height(height = candleHeight.dp)
            .offset(y = yOffset.dp)
            .background(color = backgroundColor)

    ) {
        val candleTopTailHeight = (((highPrice - openingPrice) * candleHeight) / abs(highPrice - lowPrice))
        val candleBottomTailHeight = (((openingPrice - lowPrice) * candleHeight) / abs(highPrice - lowPrice))
        val candleBodyHeight = (((abs(tradePrice - openingPrice)) * candleHeight) / abs(highPrice - lowPrice))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = candleTopTailHeight.dp)
        ) {
            CandleTail(
                candleTailWidth = candleTailWidth,
                candleTailHeight = candleTopTailHeight,
                color = color,
                modifier = Modifier.align(alignment = Alignment.BottomCenter)
            )
            if (tradePrice > openingPrice) {
                CandleBody(
                    candleBodyWidth = candleBodyWidth,
                    candleBodyHeight = candleBodyHeight,
                    color = color,
                    modifier = Modifier.align(alignment = Alignment.BottomCenter)
                )
            }
        }
        if (tradePrice == openingPrice) {
            Box(
                modifier = Modifier
                    .width(width = candleBodyWidth.dp)
                    .height(height = 2.dp)
                    .background(color = trendType.color)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = candleBottomTailHeight.dp)
        ) {
            CandleTail(
                candleTailWidth = candleTailWidth,
                candleTailHeight = candleBottomTailHeight,
                color = color,
                modifier = Modifier.align(alignment = Alignment.TopCenter)
            )
            if (tradePrice < openingPrice) {
                CandleBody(
                    candleBodyWidth = candleBodyWidth,
                    candleBodyHeight = candleBodyHeight,
                    color = color,
                    modifier = Modifier.align(alignment = Alignment.TopCenter)
                )
            }
        }
    }
}