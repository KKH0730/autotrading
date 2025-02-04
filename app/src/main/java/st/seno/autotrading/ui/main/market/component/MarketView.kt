package st.seno.autotrading.ui.main.market.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.formatPrice
import st.seno.autotrading.extensions.getBookmarkInfo
import st.seno.autotrading.extensions.getCryptoEnName
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.extensions.noRippleClickable
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF16A34A
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FF6B7280
import st.seno.autotrading.theme.FFDC2626
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.theme.FFFACC15
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.common.LeadingCandleChart

@Composable
fun MarketFavoritesView(
    tickers: List<Ticker>,
    onClickBookmark: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
        contentPadding = PaddingValues(all = 16.dp),
        modifier = Modifier.fillMaxSize().background(color = FFF9FAFB)
    ) {
        if (tickers.isEmpty()) {
            item {
                Text(
                    stringResource(R.string.home_favorite_empty),
                    style = TextStyle(
                        fontSize = 13.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF4B5563
                    ),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.textDp,
                    modifier = Modifier
                        .height(height = 300.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            }
        } else {
            items(tickers.size) { index ->
                val isAlreadyBookmarked = gson.getBookmarkInfo()[tickers[index].code] ?: false
                MarketCryptoView(ticker = tickers[index], isAlreadyBookmarked = isAlreadyBookmarked, onClickBookmark = onClickBookmark)
            }
        }
    }
}

@Composable
fun MarketView(
    tickers: List<Ticker>,
    onClickBookmark: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
        contentPadding = PaddingValues(all = 16.dp),
        modifier = Modifier.fillMaxSize().background(color = FFF9FAFB)
    ) {
        items(tickers.size) { index ->
            val isAlreadyBookmarked = gson.getBookmarkInfo()[tickers[index].code] ?: false
            MarketCryptoView(ticker = tickers[index], isAlreadyBookmarked = isAlreadyBookmarked, onClickBookmark = onClickBookmark)
        }
    }
}

@Composable
fun MarketCryptoView(
    ticker: Ticker,
    isAlreadyBookmarked: Boolean,
    onClickBookmark: (String) -> Unit
) {
    val cryptoName = gson.getCryptoEnName(key = ticker.code)
    val tickerName = ticker.code.split("-").let { if (it.size == 2) it[1] else ticker.code }
    val currencyName = ticker.code.split("-").let { if (it.size == 2) it[0] else stringResource(R.string.KRW) }

    val bookmarkStatus = remember { mutableStateOf(isAlreadyBookmarked) }

    Card(
        elevation = 1.dp,
        shape = RoundedCornerShape(size = 8.dp),
        backgroundColor = FFFFFFFF
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(height = 76.dp)
        ) {
            16.WidthSpacer()
            LeadingCandleChart(
                openingPrice = ticker.openingPrice,
                highPrice = ticker.highPrice,
                lowPrice = ticker.lowPrice,
                tradePrice = ticker.tradePrice,
                candleWidth = 7,
                candleHeight = 30
            )
            12.WidthSpacer()
            Column {
                Text(
                    cryptoName,
                    style = TextStyle(
                        fontSize = 13.textDp,
                        fontWeight = FontWeight.Medium,
                        color = FF000000
                    )
                )
                4.HeightSpacer()
                Text(
                    tickerName,
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF6B7280
                    )
                )
            }
            FullWidthSpacer()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight()
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        String.format(
                            stringResource(R.string.s_s_won),
                            ticker.tradePrice.formatPrice(),
                            currencyName
                        ),
                        style = TextStyle(
                            fontSize = 13.textDp,
                            fontWeight = FontWeight.Medium,
                            color = FF000000
                        )
                    )
                    4.HeightSpacer()
                    Row {
                        when {
                            ticker.signedChangeRate > 0.0 -> {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = FF16A34A,
                                    modifier = Modifier
                                        .size(size = 12.dp)
                                        .rotate(degrees = -90f)
                                )
                            }
                            ticker.signedChangeRate < 0.0 -> {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = FFDC2626,
                                    modifier = Modifier
                                        .size(size = 12.dp)
                                        .rotate(degrees = 90f)
                                )
                            }
                            else -> {}
                        }
                        4.WidthSpacer()
                        Text(
                            String.format(
                                stringResource(R.string.s_percent),
                                (ticker.signedChangeRate * 100).truncateToXDecimalPlaces(x = 3.0).toString()
                            ),
                            style = TextStyle(
                                fontSize = 12.textDp,
                                fontWeight = FontWeight.Normal,
                                color = when {
                                    ticker.signedChangeRate > 0.0 -> FF16A34A
                                    ticker.signedChangeRate < 0.0 -> FFDC2626
                                    else -> FF6B7280
                                }
                            )
                        )
                    }
                }
                6.WidthSpacer()
                Icon(
                    painter = if (bookmarkStatus.value) {
                        painterResource(R.drawable.ic_full_star)
                    } else {
                        painterResource(R.drawable.ic_empty_star)
                    },
                    contentDescription = null,
                    tint = FFFACC15,
                    modifier = Modifier
                        .size(size = 16.dp)
                        .noRippleClickable {
                            onClickBookmark.invoke(ticker.code)
                            bookmarkStatus.value = !bookmarkStatus.value
                        }
                )
            }
            16.WidthSpacer()
        }
    }
}

