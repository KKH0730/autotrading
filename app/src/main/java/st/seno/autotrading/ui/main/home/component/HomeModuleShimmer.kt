package st.seno.autotrading.ui.main.home.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FF6B7280
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.theme.SHIMMER
import st.seno.autotrading.ui.common.LeadingCandleChartShimmer

@Composable
fun HomeModuleShimmer() {
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

    Column {
        16.HeightSpacer()
        MyPortfolioShimmer(shimmerBrush = shimmerBrush)
        repeat(2) {
            24.HeightSpacer()
            HomeCrytoesInfoShimmer(shimmerBrush = shimmerBrush)
        }
    }
}

/**
 * Portfolio
 */

@Composable
fun MyPortfolioShimmer(shimmerBrush: Brush) {
    Card(
        backgroundColor = FFFFFFFF,
        elevation = 2.dp,
        shape = RoundedCornerShape(size = 8.dp),
        contentColor = FFFFFFFF,
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Column {
            HomeModulesTitleShimmer(shimmerBrush = shimmerBrush)
            24.HeightSpacer()
            MyPortfolioPanelShimmer(shimmerBrush = shimmerBrush)
            16.HeightSpacer()
        }
    }
}

@Composable
fun HomeModulesTitleShimmer(shimmerBrush: Brush) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp)
    ) {
        Text(
            "",
            style = TextStyle(
                fontSize = 17.textDp,
                fontWeight = FontWeight.SemiBold,
                color = FF000000
            ),
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(width = 100.dp).background(brush = shimmerBrush)
        )
        this.FullWidthSpacer()
    }
}

@Composable
fun MyPortfolioPanelShimmer(shimmerBrush: Brush) {
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
                    "",
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF4B5563
                    ),
                    modifier = Modifier.width(width = 50.dp).background(brush = shimmerBrush)
                )
                4.HeightSpacer()
                Text(
                    "",
                    style = TextStyle(
                        fontSize = 20.textDp,
                        fontWeight = FontWeight.Bold,
                        color = FF000000
                    ),
                    modifier = Modifier.width(width = 100.dp).background(brush = shimmerBrush)
                )
            }
        }
        20.HeightSpacer()
        MyCryptoesContainerShimmer(shimmerBrush = shimmerBrush)
    }
}

@Composable
fun MyCryptoesContainerShimmer(shimmerBrush: Brush) {
    Column {
        repeat(3) {
            MyCryptoShimmer(shimmerBrush = shimmerBrush)
        }
    }
}

@Composable
fun MyCryptoShimmer(shimmerBrush: Brush) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(height = 64.dp)
    ) {
        LeadingCandleChartShimmer(
            candleWidth = 7,
            candleHeight = 30,
            shimmerBrush = shimmerBrush
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
                modifier = Modifier.width(width = 50.dp).background(brush = shimmerBrush)
            )
            3.HeightSpacer()
            Text(
                "",
                style = TextStyle(
                    fontSize = 12.textDp,
                    fontWeight = FontWeight.Normal,
                    color = FF6B7280
                ),
                modifier = Modifier.width(width = 100.dp).background(brush = shimmerBrush)
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
                    color = FF000000
                ),
                modifier = Modifier.width(width = 100.dp).background(brush = shimmerBrush)
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
                        color = FF000000
                    ),
                    modifier = Modifier.width(width = 50.dp).background(brush = shimmerBrush)
                )
            }
        }
    }
}

/**
 * CryptoInfos
 */
@Composable
fun HomeCrytoesInfoShimmer(shimmerBrush: Brush) {
    Card(
        backgroundColor = FFFFFFFF,
        elevation = 2.dp,
        shape = RoundedCornerShape(size = 8.dp),
        contentColor = FFFFFFFF,
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Column {
            HomeModulesTitleShimmer(shimmerBrush = shimmerBrush)
            24.HeightSpacer()
            TopGainersOrLosersShimmer(shimmerBrush = shimmerBrush)
            16.HeightSpacer()
        }
    }
}

@Composable
fun TopGainersOrLosersShimmer(shimmerBrush: Brush) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = Modifier.padding(horizontal = 30.dp)
    ) {
        repeat(3) {
            CryptoChangeRateShimmer(shimmerBrush = shimmerBrush)
        }
    }
}

@Composable
fun CryptoChangeRateShimmer(shimmerBrush: Brush) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(height = 56.dp)
    ) {
        LeadingCandleChartShimmer(
            candleWidth = 7,
            candleHeight = 30,
            shimmerBrush = shimmerBrush
        )
        12.WidthSpacer()
        Text(
            "",
            style = TextStyle(
                fontSize = 13.textDp,
                fontWeight = FontWeight.Medium,
                color = FF000000
            ),
            modifier = Modifier.width(width = 50.dp).background(brush = shimmerBrush)
        )
        FullWidthSpacer()
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = SHIMMER,
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
                modifier = Modifier.width(width = 50.dp).background(brush = shimmerBrush)
            )
        }
    }
}