package st.seno.autotrading.ui.main.home.my_asset_overview.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.Divider
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.theme.SHIMMER
import st.seno.autotrading.ui.common.CommonToolbar

@Composable
fun MyAssetOverviewShimmer(onClickBack: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "")
    val xShimmer = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            SHIMMER.copy(alpha = 0.4f),
            SHIMMER.copy(alpha = 0.2f),
            SHIMMER.copy(alpha = 0.4f)
        ),
        start = Offset(xShimmer.value, 0f),
        end = Offset(xShimmer.value + 200f, 200f)
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CommonToolbar(
            title = stringResource(R.string.assets_overview_title),
            onClickBack = onClickBack
        )
        16.HeightSpacer()
        AssetOverviewPanelShimmer(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            shimmerBrush = shimmerBrush
        )
        16.HeightSpacer()
        CryptoOverviewPanelShimmer(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            shimmerBrush = shimmerBrush
        )
    }
}

/**
 * AssetOverview
 */

@Composable
fun AssetOverviewPanelShimmer(modifier: Modifier, shimmerBrush: Brush) {
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "",
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF4B5563
                    ),
                    modifier = Modifier
                        .width(width = 50.dp)
                        .background(brush = shimmerBrush)
                )
                4.HeightSpacer()
                Text(
                    "",
                    style = TextStyle(
                        fontSize = 20.textDp,
                        fontWeight = FontWeight.Bold,
                        color = FF000000
                    ),
                    modifier = Modifier
                        .width(width = 70.dp)
                        .background(brush = shimmerBrush)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "",
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF4B5563
                    ),
                    modifier = Modifier
                        .width(width = 50.dp)
                        .background(brush = shimmerBrush)
                )
                8.HeightSpacer()
                Text(
                    "",
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.SemiBold,
                        color = SHIMMER
                    ),
                    modifier = Modifier
                        .width(width = 70.dp)
                        .background(brush = shimmerBrush)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "",
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = SHIMMER
                    ),
                    modifier = Modifier
                        .width(width = 50.dp)
                        .background(brush = shimmerBrush)
                )
                8.HeightSpacer()
                Text(
                    "",
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.SemiBold,
                        color = SHIMMER
                    ),
                    modifier = Modifier
                        .width(width = 70.dp)
                        .background(brush = shimmerBrush)
                )
            }
        }
    }
}

/**
 * CryptoOverview
 */
@Composable
fun CryptoOverviewPanelShimmer(modifier: Modifier, shimmerBrush: Brush) {
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(size = 12.dp),
        backgroundColor = FFFFFFFF,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            CryptoOverviewTitleShimmer(shimmerBrush = shimmerBrush)
            CryptoOverviewContainerShimmer(shimmerBrush = shimmerBrush)
        }
    }
}

@Composable
fun CryptoOverviewTitleShimmer(shimmerBrush: Brush) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            elevation = 0.dp,
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            backgroundColor = FFFFFFFF,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "",
                style = TextStyle(
                    fontSize = 13.textDp,
                    fontWeight = FontWeight.SemiBold,
                    color = FF000000
                ),
                modifier = Modifier
                    .width(width = 50.dp)
                    .padding(vertical = 14.dp, horizontal = 24.dp)
                    .background(brush = shimmerBrush)
            )
        }
        1.Divider()
    }
}

@Composable
fun CryptoOverviewContainerShimmer(shimmerBrush: Brush) {
    repeat(5) { index ->
        CryptoShimmer(shimmerBrush = shimmerBrush)
        if (index != 4) {
            1.Divider()
        }
    }
}

@Composable
fun CryptoShimmer(shimmerBrush: Brush) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_bitcoin_default),
            contentDescription = null,
            alpha = 0f,
            modifier = Modifier
                .size(size = 32.dp)
                .background(brush = shimmerBrush)
        )
        12.WidthSpacer()
        Column {
            Text(
                "",
                style = TextStyle(
                    fontSize = 13.textDp,
                    fontWeight = FontWeight.Medium,
                    color = FF000000
                ),
                modifier = Modifier
                    .width(width = 50.dp)
                    .background(brush = shimmerBrush)
            )
            3.HeightSpacer()
            Text(
                "",
                style = TextStyle(
                    fontSize = 12.textDp,
                    fontWeight = FontWeight.Normal,
                    color = SHIMMER
                ),
                modifier = Modifier
                    .width(width = 50.dp)
                    .background(brush = shimmerBrush)
            )
        }
        FullWidthSpacer()
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                "",
                style = TextStyle(
                    fontSize = 13.textDp,
                    fontWeight = FontWeight.Medium,
                    color = SHIMMER
                ),
                modifier = Modifier
                    .width(width = 50.dp)
                    .background(brush = shimmerBrush)
            )
            3.HeightSpacer()
            Text(
                "",
                style = TextStyle(
                    fontSize = 12.textDp,
                    fontWeight = FontWeight.Normal,
                    color = SHIMMER
                ),
                modifier = Modifier
                    .width(width = 100.dp)
                    .background(brush = shimmerBrush)
            )
            3.HeightSpacer()
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size = 12.dp)
                        .alpha(alpha = 0f)
                        .background(brush = shimmerBrush)
                )
                4.WidthSpacer()
                Text(
                    "",
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = SHIMMER
                    ),
                    modifier = Modifier
                        .width(width = 50.dp)
                        .background(brush = shimmerBrush)
                )
            }
        }
    }
}