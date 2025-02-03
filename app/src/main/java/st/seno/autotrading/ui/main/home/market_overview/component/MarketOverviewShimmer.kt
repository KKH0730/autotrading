package st.seno.autotrading.ui.main.home.market_overview.component

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF2563EB
import st.seno.autotrading.theme.FF6B7280
import st.seno.autotrading.theme.FFF8F8F8
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.theme.SHIMMER
import st.seno.autotrading.ui.common.CommonToolbar
import st.seno.autotrading.ui.main.home.market_overview.marketOverviewTabs

@Composable
fun MarketOverviewShimmer(
    onClickBack: () -> Unit
) {
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
            title = stringResource(R.string.market_overview_title),
            onClickBack = onClickBack
        )
        Column {
            MarketOverviewTabRowShimmer(
                shimmerBrush = shimmerBrush,
                tabIndex = 0,
            )

            TopGainersOverviewShimmer(shimmerBrush = shimmerBrush)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketOverviewTabRowShimmer(
    shimmerBrush: Brush,
    tabIndex: Int,
) {
    SecondaryTabRow(
        selectedTabIndex = tabIndex,
        containerColor = FFFFFFFF,
        contentColor = FFF8F8F8,
        indicator = {
            SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabIndex, matchContentSize = false),
                color = SHIMMER
            )
        }
    ) {
        marketOverviewTabs.forEachIndexed { index, _ ->
            MarketOverviewTabShimmer(
                shimmerBrush = shimmerBrush,
                tabIndex = tabIndex,
                position = index,
            )
        }
    }
}

@Composable
fun MarketOverviewTabShimmer(
    shimmerBrush: Brush,
    tabIndex: Int,
    position: Int,
) {
    Tab(
        selected = tabIndex == position,
        selectedContentColor = FF2563EB,
        unselectedContentColor = FF6B7280,
        onClick = { }
    ) {
        Text(
            text = "",
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = SHIMMER
            ),
            modifier = Modifier
                .width(width = 50.dp)
                .padding(vertical = 16.dp)
                .background(brush = shimmerBrush)
        )
    }
}

@Composable
fun TopGainersOverviewShimmer(shimmerBrush: Brush) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
        contentPadding = PaddingValues(all = 16.dp),
        modifier = Modifier.background(color = FFF9FAFB)
    ) {
        items(10) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                modifier = Modifier.padding(horizontal = 30.dp)
            ) {
                CryptoOverviewChangeRateShimmer(shimmerBrush = shimmerBrush)
            }
        }
    }
}

@Composable
fun CryptoOverviewChangeRateShimmer(shimmerBrush: Brush) {
    Card(
        elevation = 1.dp,
        shape = RoundedCornerShape(size = 8.dp),
        backgroundColor = FFFFFFFF
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 22.dp, horizontal = 16.dp)
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
                    modifier = Modifier
                        .width(width = 50.dp)
                        .background(brush = shimmerBrush)
                )
            }
        }
    }
}