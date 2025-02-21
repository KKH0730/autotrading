package st.seno.autotrading.ui.main.trading_view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import st.seno.autotrading.theme.Transparent

@Composable
fun CandleBlank(
    width: Int,
    height: Int
) {
    Box(
        modifier = Modifier
            .width(width = width.dp)
            .height(height = height.dp)
            .background(color = Transparent)
    )
}