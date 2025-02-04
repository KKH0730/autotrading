package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000

@Composable
fun AiTradingModelPanel() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Text(
            "Preparing",
            style = TextStyle(
                fontSize = 14.textDp,
                fontWeight = FontWeight.Medium,
                color = FF000000
            ),
            modifier = Modifier
                .align(alignment = Alignment.Center)
        )
    }
}