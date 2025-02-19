package st.seno.autotrading.ui.main.trading_view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.formatRealPrice
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.model.PriceTrend
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF9CA3AF
import st.seno.autotrading.ui.main.trading_view.tradingViewHeaderHeight

@Composable
fun TradingViewHeader(ticker: Ticker) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(height = tradingViewHeaderHeight.dp)
            .padding(horizontal = 24.dp)
    ) {
        val priceTrend = PriceTrend.getPriceTrend(targetPrice = ticker.tradePrice, openingPrice = ticker.openingPrice)
        PriceAndChangeRatePanel(
            ticker = ticker,
            priceTrend = priceTrend,
            modifier = Modifier.weight(weight = 0.6f)
        )
        AdditionalTickerInfoPanel(
            ticker = ticker,
            modifier = Modifier.weight(weight = 0.4f)
        )
    }
}

//region(PriceAndChangeRatePanel)
@Composable
fun PriceAndChangeRatePanel(
    ticker: Ticker,
    priceTrend: PriceTrend,
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 5.dp, alignment = Alignment.CenterVertically),
        modifier = modifier
    ) {
        Text(
            ticker.tradePrice.formatRealPrice(),
            style = TextStyle(
                fontSize = 22.textDp,
                fontWeight = FontWeight.Bold,
                color = priceTrend.color
            )
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${(ticker.signedChangeRate * 100).truncateToXDecimalPlaces(x = 2.0)}%",
                style = TextStyle(
                    fontSize = 12.textDp,
                    fontWeight = FontWeight.Medium,
                    color = priceTrend.color
                )
            )
            15.WidthSpacer()
            when (priceTrend) {
                PriceTrend.RISE, PriceTrend.FALL -> {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = priceTrend.color,
                        modifier = Modifier
                            .size(size = 12.dp)
                            .rotate(degrees = if (priceTrend == PriceTrend.RISE) -90f else 90f)
                    )
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .width(width = 8.dp)
                            .height(height = 2.dp)
                            .background(color = priceTrend.color)
                    )
                }
            }
            5.WidthSpacer()
            Text(
                ticker.signedChangePrice.formatRealPrice(),
                style = TextStyle(
                    fontSize = 12.textDp,
                    fontWeight = FontWeight.Medium,
                    color = priceTrend.color
                )
            )
        }
    }
}
//endregion

@Composable
fun AdditionalTickerInfoPanel(
    ticker: Ticker,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 36.dp, alignment = Alignment.End),
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(space = 4.dp)) {
                Text(
                    stringResource(R.string.trading_view_header_high),
                    style = TextStyle(
                        fontSize = 9.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF9CA3AF
                    )
                )
                Text(
                    ticker.highPrice.formatRealPrice(),
                    style = TextStyle(
                        fontSize = 9.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF000000
                    )
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(space = 4.dp)) {
                Text(
                    stringResource(R.string.trading_view_header_low),
                    style = TextStyle(
                        fontSize = 9.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF9CA3AF
                    )
                )
                Text(
                    ticker.lowPrice.formatRealPrice(),
                    style = TextStyle(
                        fontSize = 9.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF000000
                    )
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(space = 4.dp)) {
                Text(
                    stringResource(R.string.trading_view_header_vol),
                    style = TextStyle(
                        fontSize = 9.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF9CA3AF
                    )
                )
                Text(
                    ticker.accTradeVolume.truncateToXDecimalPlaces(x = 2.0).toString(),
                    style = TextStyle(
                        fontSize = 9.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF000000
                    )
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(space = 4.dp)) {
                Text(
                    stringResource(R.string.trading_view_header_24_vol),
                    style = TextStyle(
                        fontSize = 9.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF9CA3AF
                    )
                )
                Text(
                    ticker.accTradeVolume24h.truncateToXDecimalPlaces(x = 2.0).toString(),
                    style = TextStyle(
                        fontSize = 9.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF000000
                    )
                )
            }
        }
    }
}