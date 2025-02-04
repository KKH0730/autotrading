package st.seno.autotrading.ui.main.home.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.formatPrice
import st.seno.autotrading.extensions.getCryptoEnName
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.model.HomeContentsType
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF16A34A
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FF6B7280
import st.seno.autotrading.theme.FFDC2626
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.main.home.my_asset_overview.MyAssetOverviewActivity

@Composable
fun MyPortfolio(data: HomeContentsType.Portfolio) {
    val context = LocalContext.current

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
                isShowViewMore = data.totalAsset.assets.size > 2,
                onClickShowMore = { MyAssetOverviewActivity.start(context = context) }
            )
            24.HeightSpacer()
            MyPortfolioPanel(data = data)
            16.HeightSpacer()
        }
    }
}

@Composable
fun MyPortfolioPanel(data: HomeContentsType.Portfolio) {
    Column(
        modifier = Modifier.padding(horizontal = 30.dp)
    ) {
        Card(
            elevation = 0.dp,
            shape = RoundedCornerShape(size = 8.dp),
            backgroundColor = FFF9FAFB,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(all = 16.dp)) {
                Text(
                    stringResource(R.string.home_total_Portfolio_value),
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF4B5563
                    ),
                )
                4.HeightSpacer()
                Text(
                    String.format(
                        stringResource(R.string.s_won),
                        data.totalAsset.totalEvaluatedPrice.formatPrice()
                    ),
                    style = TextStyle(
                        fontSize = 20.textDp,
                        fontWeight = FontWeight.Bold,
                        color = FF000000
                    )
                )
            }
        }
        20.HeightSpacer()
        MyCryptoesContainer(data = data)
    }
}

@Composable
fun MyCryptoesContainer(data: HomeContentsType.Portfolio) {
    val list = if (data.totalAsset.assets.size > 3) {
        data.totalAsset.assets.slice(0..2)
    } else {
        data.totalAsset.assets
    }

    Column {
        list.forEach { asset ->
            MyCrypto(asset = asset)
        }
    }
}

@Composable
fun MyCrypto(asset: Asset) {
    val cryptoName = gson.getCryptoEnName(key = "${stringResource(R.string.KRW)}-${asset.currency}")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(height = 64.dp)
    ) {
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