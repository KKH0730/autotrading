package st.seno.autotrading.ui.main.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
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
import st.seno.autotrading.extensions.getCryptoEnName
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.extensions.noRippleClickable
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.model.HomeContentsType
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF16A34A
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FF6B7280
import st.seno.autotrading.theme.FFDC2626
import st.seno.autotrading.theme.FFFACC15
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.common.LeadingCandleView
import st.seno.autotrading.ui.main.home.market_overview.MarketOverviewActivity

@Composable
fun HomeCrytoesInfo(
    data: HomeContentsType,
    onClickCryptoItem:(String) -> Unit,
    onClickBookmark: ((String) -> Unit)? = null
) {
    val context = LocalContext.current

    val tickers = when (data) {
        is HomeContentsType.TopGainers -> data.tickers
        is HomeContentsType.TopLosers -> data.tickers
        is HomeContentsType.Favorites -> data.tickers
        else -> listOf()
    }

    Card(
        backgroundColor = FFFFFFFF,
        elevation = 2.dp,
        shape = RoundedCornerShape(size = 8.dp),
        contentColor = FFFFFFFF,
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Column {
            HomeModulesTitle(
                data = data,
                isShowViewMore = tickers.size > 3,
                onClickShowMore = { tabPosition ->
                    MarketOverviewActivity.start(context = context, initialPosition = tabPosition)
                }
            )
            24.HeightSpacer()
            if (data is HomeContentsType.Favorites && tickers.isEmpty()) {
                Text(
                    stringResource(R.string.home_favorite_empty),
                    style = TextStyle(
                        fontSize = 13.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF4B5563
                    ),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.textDp,
                    modifier = Modifier.fillMaxWidth()
                )
                24.HeightSpacer()
            } else {
                when (data) {
                    is HomeContentsType.TopGainers -> TopGainers(tickers = data.tickers, onClickCryptoItem = onClickCryptoItem)
                    is HomeContentsType.TopLosers -> TopLosers(tickers = data.tickers, onClickCryptoItem = onClickCryptoItem)
                    is HomeContentsType.Favorites -> FavoritesCryptoes(
                        tickers = data.tickers,
                        onClickCryptoItem = onClickCryptoItem,
                        onClickBookmark = onClickBookmark
                    )
                    else -> Box {}
                }
                16.HeightSpacer()
            }
        }
    }
}

@Composable
fun TopGainers(
    tickers: List<Ticker>,
    onClickCryptoItem:(String) -> Unit,
) {
    val list = if (tickers.size > 3) {
        tickers.slice(0..2)
    } else {
        tickers
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = Modifier.padding(horizontal = 30.dp)
    ) {
        list.forEach { ticker ->
            CryptoChangeRate(ticker = ticker, onClickCryptoItem = onClickCryptoItem)
        }
    }
}

@Composable
fun TopLosers(
    tickers: List<Ticker>,
    onClickCryptoItem:(String) -> Unit,
) {
    val list = if (tickers.size > 3) {
        tickers.slice(0..2)
    } else {
        tickers
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = Modifier.padding(horizontal = 30.dp)
    ) {
        list.forEach { ticker ->
            CryptoChangeRate(ticker = ticker, onClickCryptoItem = onClickCryptoItem)
        }
    }
}

@Composable
fun FavoritesCryptoes(
    tickers: List<Ticker>,
    onClickCryptoItem:(String) -> Unit,
    onClickBookmark: ((String) -> Unit)?
) {
    val list = if (tickers.size > 3) {
        tickers.slice(0..2)
    } else {
        tickers
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = Modifier.padding(horizontal = 30.dp)
    ) {
        list.forEach { ticker ->
            CryptoChangeRate(
                ticker = ticker,
                isAlreadyBookmarked = true,
                onClickCryptoItem = onClickCryptoItem,
                onClickBookmark = onClickBookmark
            )
        }
    }
}

@Composable
fun CryptoChangeRate(
    ticker: Ticker,
    isAlreadyBookmarked: Boolean = false,
    onClickCryptoItem:(String) -> Unit,
    onClickBookmark: ((String) -> Unit)? = null
) {
    val cryptoName = gson.getCryptoEnName(key = ticker.code)
    val currencyName = ticker.code.split("-").let { if (it.size == 2) it[0] else stringResource(R.string.KRW) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(height = 56.dp)
            .noRippleClickable { onClickCryptoItem.invoke(ticker.code) }
    ) {
        LeadingCandleView(
            openingPrice = ticker.openingPrice,
            highPrice = ticker.highPrice,
            lowPrice = ticker.lowPrice,
            tradePrice = ticker.tradePrice,
            candleWidth = 7,
            candleHeight = 30,

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
                String.format(
                    stringResource(R.string.s_s_won),
                    ticker.tradePrice.formatPrice(),
                    currencyName
                ),
                style = TextStyle(
                    fontSize = 12.textDp,
                    fontWeight = FontWeight.Normal,
                    color = FF6B7280
                )
            )
        }
        FullWidthSpacer()
        Row(verticalAlignment = Alignment.CenterVertically) {
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
                    (ticker.signedChangeRate * 100).truncateToXDecimalPlaces(x = 2.0).toString()
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
            if (onClickBookmark != null) {
                6.WidthSpacer()
                Icon(
                    painter = if (isAlreadyBookmarked) {
                        painterResource(R.drawable.ic_full_star)
                    } else {
                        painterResource(R.drawable.ic_empty_star)
                    },
                    contentDescription = null,
                    tint = FFFACC15,
                    modifier = Modifier
                        .size(size = 16.dp)
                        .noRippleClickable { onClickBookmark.invoke(ticker.code) }
                )
            }
        }
    }
}