package st.seno.autotrading.ui.main.home.my_asset_overview.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.formatPrice
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.model.TotalAsset
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF16A34A
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FF6B7280
import st.seno.autotrading.theme.FFDC2626
import st.seno.autotrading.theme.FFFFFFFF

@Composable
fun AssetOverviewPanel(data: TotalAsset, modifier: Modifier) {
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(size = 12.dp),
        backgroundColor = FFFFFFFF,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.padding(vertical = 24.dp)
        ) {
            Column {
                Text(
                    stringResource(R.string.home_total_value),
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
                        data.totalEvaluatedPrice.formatPrice()
                    ),
                    style = TextStyle(
                        fontSize = 20.textDp,
                        fontWeight = FontWeight.Bold,
                        color = FF000000
                    )
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    stringResource(R.string.assets_overview_pnl),
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF4B5563
                    ),
                )
                8.HeightSpacer()
                Text(
                    String.format(
                        stringResource(R.string.s_won),
                        (data.totalEvaluatedPrice * data.totalSignedChangeRate).formatPrice()
                    ),
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.SemiBold,
                        color = when {
                            data.totalSignedChangeRate > 0.0 -> FF16A34A
                            data.totalSignedChangeRate < 0.0 -> FFDC2626
                            else -> FF6B7280
                        }
                    )
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    stringResource(R.string.assets_overview_roi),
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF4B5563
                    ),
                )
                8.HeightSpacer()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    when {
                        data.totalSignedChangeRate > 0.0 -> {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = FF16A34A,
                                modifier = Modifier
                                    .size(size = 12.dp)
                                    .rotate(degrees = -90f)
                            )
                        }

                        data.totalSignedChangeRate < 0.0 -> {
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
                            (data.totalSignedChangeRate * 100).truncateToXDecimalPlaces(x = 2.0).toString()
                        ),
                        style = TextStyle(
                            fontSize = 12.textDp,
                            fontWeight = FontWeight.SemiBold,
                            color = when {
                                data.totalSignedChangeRate > 0.0 -> FF16A34A
                                data.totalSignedChangeRate < 0.0 -> FFDC2626
                                else -> FF6B7280
                            }
                        )
                    )

                }
            }
        }
    }
}


