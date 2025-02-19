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
                        isAutoTradingView = intent.hasExtra(AUTO_TRADING_START_DATE),
                        onClickBack = { finish() }
                    )
                }
            }
        }
    }

    companion object {
        const val TICKER_CODE = "tickerCode"
        const val AUTO_TRADING_START_DATE = "autoTradingStartDate"

        fun start(context: Context, tickerCode: String, autoTradingStartDate: String = "") {
            context.startActivity(TradingViewActivity::class.java) {
                putExtra(TICKER_CODE, tickerCode)
                autoTradingStartDate.takeIf { it.isNotEmpty() }?.let { putExtra(AUTO_TRADING_START_DATE, it) }
            }
        }
    }
}