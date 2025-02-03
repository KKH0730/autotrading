package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FFFFFFFF

@Composable
fun AvailableKrwPanel() {
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(size = 8.dp),
        backgroundColor = FFFFFFFF,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Column {
                24.HeightSpacer()
                Text(
                    stringResource(R.string.auto_trading_available_krw),
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF4B5563
                    ),
                    overflow = TextOverflow.Ellipsis,
                )
                4.HeightSpacer()
                Text(
                    "0 KRW",
                    style = TextStyle(
                        fontSize = 20.textDp,
                        fontWeight = FontWeight.Bold,
                        color = FF000000
                    ),
                    overflow = TextOverflow.Ellipsis,
                )
                24.HeightSpacer()
            }
        }
    }
}