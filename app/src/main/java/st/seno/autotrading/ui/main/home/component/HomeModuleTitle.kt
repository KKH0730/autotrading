package st.seno.autotrading.ui.main.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.model.HomeContentsType
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF2563EB

@Composable
fun HomeModulesTitle(
    data: HomeContentsType,
    isShowViewMore: Boolean = false,
    onClickShowMore: (Int) -> Unit
) {
    val title = when (data) {
        is HomeContentsType.Portfolio -> stringResource(R.string.home_my_portfolio)
        is HomeContentsType.TopGainers -> stringResource(R.string.home_top_gainers)
        is HomeContentsType.TopLosers -> stringResource(R.string.home_top_losers)
        is HomeContentsType.Favorites -> stringResource(R.string.home_favorite)
    }

    val tabPosition = when (data) {
        is HomeContentsType.Portfolio -> -1
        is HomeContentsType.Favorites -> 0
        is HomeContentsType.TopGainers -> 1
        is HomeContentsType.TopLosers -> 2
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp)
    ) {
        Text(
            title,
            style = TextStyle(
                fontSize = 17.textDp,
                fontWeight = FontWeight.SemiBold,
                color = FF000000
            ),
            overflow = TextOverflow.Ellipsis,
        )
        this.FullWidthSpacer()
        if (isShowViewMore) {
            Row(modifier = Modifier.clickable { onClickShowMore.invoke(tabPosition) }) {
                Text(
                    stringResource(R.string.view_more),
                    style = TextStyle(
                        fontSize = 13.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF2563EB
                    ),
                    overflow = TextOverflow.Ellipsis,
                )
                4.WidthSpacer()
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = FF2563EB,
                    modifier = Modifier.size(size = 16.dp)
                )
            }
        }
    }
}