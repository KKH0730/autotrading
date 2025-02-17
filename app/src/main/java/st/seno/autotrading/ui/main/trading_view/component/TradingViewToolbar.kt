package st.seno.autotrading.ui.main.trading_view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.extensions.noRippleClickable
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FFFACC15
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.main.trading_view.tradingViewToolbarHeight

@Composable
fun TradingViewToolbar(
    title: String,
    tickerCode: String,
    isAlreadyBookmarked: Boolean,
    onClickBookmark: (String) -> Unit,
    onClickBack: () -> Unit
) {
    Card(
        elevation = 0.dp,
        backgroundColor = FFFFFFFF,
        modifier = Modifier
            .fillMaxWidth()
            .height(height = tradingViewToolbarHeight.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(size = 36.dp)
                    .offset(x = 16.dp)
                    .clip(shape = CircleShape)
                    .clickable { onClickBack.invoke() }
                    .align(alignment = Alignment.CenterStart)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = FF000000,
                    modifier = Modifier
                        .size(size = 20.dp)
                        .align(alignment = Alignment.Center)
                )
            }
            Text(
                title,
                style = TextStyle(
                    fontSize = 17.textDp,
                    fontWeight = FontWeight.SemiBold,
                    color = FF000000
                ),
                modifier = Modifier.align(alignment = Alignment.Center)
            )
            Icon(
                painter = if (isAlreadyBookmarked) {
                    painterResource(R.drawable.ic_full_star)
                } else {
                    painterResource(R.drawable.ic_empty_star)
                },
                contentDescription = null,
                tint = FFFACC15,
                modifier = Modifier
                    .size(size = 20.dp)
                    .align(alignment = Alignment.CenterEnd)
                    .offset(x = (-16).dp)
                    .noRippleClickable { onClickBookmark.invoke(tickerCode) }
            )
        }
    }
}