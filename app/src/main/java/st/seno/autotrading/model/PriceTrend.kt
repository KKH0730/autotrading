package st.seno.autotrading.model

import androidx.compose.ui.graphics.Color
import com.pixplicity.easyprefs.library.Prefs
import st.seno.autotrading.prefs.PrefsManager
import st.seno.autotrading.theme.FF16A34A
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FFDC2626

enum class PriceTrend(val value: String, val color: Color) {
    RISE(value = "RISE", color = Color(PrefsManager.riseColor)), // 상승
    FALL(value = "FALL", color = Color(PrefsManager.fallColor)), // 하락
    EVEN(value = "EVEN", color = Color(PrefsManager.evenColor)); // 중립

    companion object {
        fun getPriceTrend(targetPrice: Double, openingPrice: Double): PriceTrend {
            return if (targetPrice > openingPrice) {
                RISE
            } else if (targetPrice < openingPrice ){
                FALL
            } else {
                EVEN
            }
        }
    }
}