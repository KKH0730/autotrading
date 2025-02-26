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
    date: String,
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
    if (date == "2025-02-26 14:28:00") {
        Timber.e("highPrice: $highPrice")
        Timber.e("lowPrice: $lowPrice")
        Timber.e("tradePrice: $tradePrice")
        Timber.e("openingPrice: $openingPrice")
        Timber.e("trendType: $trendType")
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
                trendType = trendType,
                candleTailWidth = candleTailWidth,
                candleTailHeight = candleTopTailHeight,
                modifier = Modifier.align(alignment = Alignment.BottomCenter)
            )
            if (tradePrice > openingPrice) {
                CandleBody(
                    trendType = trendType,
                    candleBodyWidth = candleBodyWidth,
                    candleBodyHeight = candleBodyHeight,
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
                trendType = trendType,
                candleTailWidth = candleTailWidth,
                candleTailHeight = candleBottomTailHeight,
                modifier = Modifier.align(alignment = Alignment.TopCenter)
            )
            if (tradePrice < openingPrice) {
                CandleBody(
                    trendType = trendType,
                    candleBodyWidth = candleBodyWidth,
                    candleBodyHeight = candleBodyHeight,
                    modifier = Modifier.align(alignment = Alignment.TopCenter)
                )
            }
        }
    }
}

//region(CandleTail)
@Composable
fun CandleTail(
    trendType: PriceTrend,
    candleTailWidth: Double,
    candleTailHeight: Double,
    modifier: Modifier
) {
    Spacer(
        modifier = modifier
            .width(width = candleTailWidth.dp)
            .height(height = candleTailHeight.dp)
            .background(color = trendType.color)
    )
}
//endregion

@Composable
fun CandleBody(
    trendType: PriceTrend,
    candleBodyWidth: Int,
    candleBodyHeight: Double,
    modifier: Modifier
) {
    Spacer(
        modifier = modifier
            .width(width = candleBodyWidth.dp)
            .height(height = candleBodyHeight.dp)
            .background(color = trendType.color)
    )
}