package st.seno.autotrading.ui.main.trading_view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import st.seno.autotrading.model.PriceTrend

@Composable
fun VolumeCandleView(
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