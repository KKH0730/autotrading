package st.seno.autotrading.ui.main.home.my_asset_overview.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.extensions.Divider
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.formatPrice
import st.seno.autotrading.extensions.getCryptoEnName
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF16A34A
import st.seno.autotrading.theme.FF6B7280
import st.seno.autotrading.theme.FFDC2626
import st.seno.autotrading.theme.FFFFFFFF

@Composable
fun CryptoOverviewPanel(assets: List<Asset>, modifier: Modifier) {
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(size = 12.dp),
        backgroundColor = FFFFFFFF,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            CryptoOverviewTitle()
            CryptoOverviewContainer(assets = assets)
        }
    }
}

@Composable
fun CryptoOverviewTitle() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            elevation = 0.dp,
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            backgroundColor = FFFFFFFF,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.assets_overview_assets),
                style = TextStyle(
                    fontSize = 13.textDp,
                    fontWeight = FontWeight.SemiBold,
                    color = FF000000
                ),
                modifier = Modifier.padding(vertical = 14.dp, horizontal = 24.dp)
            )
        }
        1.Divider()
    }
}

@Composable
fun CryptoOverviewContainer(assets: List<Asset>) {
    LazyColumn {
        items(assets.size) { index ->
            Crypto(asset = assets[index])
            if (index != assets.lastIndex) {
                1.Divider()
            }
        }
    }
}

@Composable
fun Crypto(asset: Asset) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
    ) {
        val cryptoName = gson.getCryptoEnName(key = "${stringResource(R.string.KRW)}-${asset.currency}")
        Column {
            Text(
                cryptoName.ifEmpty { asset.currency },
                style = TextStyle(
                    fontSize = 13.textDp,
                    fontWeight = FontWeight.Medium,
                    color = FF000000
                )
            )
            3.HeightSpacer()
            Text(
                "${asset.balance} ${asset.currency}",
                style = TextStyle(
                    fontSize = 12.textDp,
                    fontWeight = FontWeight.Normal,
                    color = FF6B7280
                )
            )
        }
        FullWidthSpacer()
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                String.format(
                    stringResource(R.string.s_won),
                    asset.evaluatedPrice.formatPrice()
                ),
                style = TextStyle(
                    fontSize = 13.textDp,
                    fontWeight = FontWeight.Medium,
                    color = FF000000
                ),
            )
            3.HeightSpacer()
            Text(
                String.format(
                    stringResource(R.string.s_won),
                    asset.avgBuyPrice.toDouble().formatPrice()
                ),
                style = TextStyle(
                    fontSize = 12.textDp,
                    fontWeight = FontWeight.Normal,
                    color = FF6B7280
                )
            )
            3.HeightSpacer()
            Row(verticalAlignment = Alignment.CenterVertically) {
                when {
                    asset.signedChangeRate > 0.0 -> {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = FF16A34A,
                            modifier = Modifier
                                .size(size = 12.dp)
                                .rotate(degrees = -90f)
                        )
                    }

                    asset.signedChangeRate < 0.0 -> {
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
                        (asset.signedChangeRate * 100).truncateToXDecimalPlaces(x = 2.0).toString()
                    ),
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = when {
                            asset.signedChangeRate > 0.0 -> FF16A34A
                            asset.signedChangeRate < 0.0 -> FFDC2626
                            else -> FF6B7280
                        }
                    )
                )
            }
        }
    }
}