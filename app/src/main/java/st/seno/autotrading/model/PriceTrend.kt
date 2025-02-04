package st.seno.autotrading.model

import androidx.compose.ui.graphics.Color
import st.seno.autotrading.theme.FF16A34A
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FFDC2626

enum class PriceTrend(val color: Color) {
    BULLISH(color = FF16A34A), // 상승
    BEARISH(color = FFDC2626), // 하락
    NEUTRAL(color = FF4B5563) // 중립
}