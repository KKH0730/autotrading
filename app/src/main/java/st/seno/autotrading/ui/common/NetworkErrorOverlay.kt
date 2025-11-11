package st.seno.autotrading.ui.common

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF2563EB
import st.seno.autotrading.theme.FF6B7280
import st.seno.autotrading.theme.FFBDBBBB
import st.seno.autotrading.theme.FFFFFFFF

@Composable
fun AutoTradingNetworkErrorOverlay(
    onClickRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size = 120.dp)
                .clip(shape = CircleShape)
                .background(color = FFBDBBBB)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_wifi_off),
                contentDescription = null,
                tint = FFFFFFFF,
                modifier = Modifier
                    .size(size = 70.dp)
                    .align(alignment = Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(height = 25.dp))
        Text(
            stringResource(R.string.network_error_title),
            style = TextStyle(
                color = FF000000,
                fontSize = 18.textDp,
                fontWeight = FontWeight.W500
            )
        )
        Spacer(modifier = Modifier.height(height = 10.dp))
        Text(
            stringResource(R.string.network_error),
            style = TextStyle(
                color = FF6B7280,
                fontSize = 16.textDp,
                fontWeight = FontWeight.W400
            )
        )
        Spacer(modifier = Modifier.height(height = 30.dp))
        Box(
            modifier = Modifier
                .width(width = 300.dp)
                .height(height = 42.dp)
                .clip(shape = RoundedCornerShape(size = 12.dp))
                .background(color = FF2563EB)
                .clickable(
                    onClick = onClickRetry,
                    indication = LocalIndication.current,
                    interactionSource = remember { MutableInteractionSource() }
                )
        ) {
            Text(
                stringResource(R.string.network_error_retry),
                style = TextStyle(
                    color = FFFFFFFF,
                    fontSize = 16.textDp,
                    fontWeight = FontWeight.W500
                ),
                modifier = Modifier.align(alignment = Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(height = 10.dp))
        Text(
            stringResource(R.string.check_network_settings),
            style = TextStyle(
                color = FF6B7280,
                fontSize = 15.textDp,
                fontWeight = FontWeight.W400,
                textAlign = TextAlign.Center
            )
        )
    }
}