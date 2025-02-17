package st.seno.autotrading.ui.main.trading_view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.extensions.noRippleClickable
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FFBDBBBB
import st.seno.autotrading.ui.main.trading_view.candleTimeFrames
import st.seno.autotrading.ui.main.trading_view.candleTimeframeHeight

@Composable
fun CandleTimeframe(
    selectedTimeFrame: String,
    onClickTimeFrame: (Pair<String, Int>) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(height = candleTimeframeHeight.dp)
            .padding(horizontal = 16.dp)
    ) {
        candleTimeFrames.forEach {
            TimeFrameItem(
                name = it.first,
                num = it.second,
                isSelected = it.first == selectedTimeFrame,
                onClickTimeFrame = onClickTimeFrame,
            )
        }
    }
}
@Composable
fun TimeFrameItem(
    name: String,
    num: Int,
    isSelected: Boolean,
    onClickTimeFrame: (Pair<String, Int>) -> Unit
) {
    Box(modifier = Modifier.noRippleClickable { onClickTimeFrame.invoke(name to num) }) {
        Text(
            name,
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Normal,
                color = if (isSelected) FF000000 else FFBDBBBB
            ),
            modifier = Modifier.align(alignment = Alignment.Center)
        )
    }
}