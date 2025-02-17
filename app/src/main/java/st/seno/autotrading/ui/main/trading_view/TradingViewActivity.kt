package st.seno.autotrading.ui.main.trading_view

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
class TradingViewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AutotradingTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TradingViewScreen(
                        tickerCode = intent.getStringExtra(TICKER_CODE) ?: "",
                        onClickBack = { finish() }
                    )
                }
            }
        }
    }

    companion object {
        const val TICKER_CODE = "tickerCode"

        fun start(context: Context, tickerCode: String) {
            context.startActivity(TradingViewActivity::class.java) {
                putExtra(TICKER_CODE, tickerCode)
            }
        }
    }
}