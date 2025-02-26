package st.seno.autotrading.ui.main.settings.color_preference

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import st.seno.autotrading.extensions.restartApp
import st.seno.autotrading.extensions.startActivity
import st.seno.autotrading.prefs.PrefsManager
import st.seno.autotrading.theme.AutotradingTheme

@AndroidEntryPoint
class ColorPreferenceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AutotradingTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val colors = listOf(
                        Triple(0xFF16A34A, 0xFFDC2626, 0xFF4B5563),
                        Triple(0xFFDC2626, 0xFF001BE8, 0xFF4B5563),
                        Triple(0xFF16A34A, 0xFF001BE8, 0xFF4B5563),
                    )

                    ColorPreferenceScreen(
                        colors = colors,
                        onClickStyle = {
                            lifecycleScope.launch {
                                PrefsManager.apply {
                                    selectedColorIndex = it
                                    riseColor = colors[it].first
                                    fallColor = colors[it].second
                                    evenColor = colors[it].third
                                }
                                delay(500)
                                this@ColorPreferenceActivity.restartApp()
                            }
                        },
                        onClickBack = { finish() }
                    )
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(ColorPreferenceActivity::class.java)
        }
    }
}