package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_backtest

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import st.seno.autotrading.extensions.startActivity
import st.seno.autotrading.theme.AutotradingTheme

@AndroidEntryPoint
class BackTestActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AutotradingTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BacktestScreen(
                        onClickBack = { finish() }
                    )
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(BackTestActivity::class.java)
        }
    }
}