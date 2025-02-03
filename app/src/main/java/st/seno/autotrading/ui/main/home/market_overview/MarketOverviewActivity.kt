package st.seno.autotrading.ui.main.home.market_overview

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dagger.hilt.android.AndroidEntryPoint
import st.seno.autotrading.R
import st.seno.autotrading.extensions.startActivity
import st.seno.autotrading.theme.AutotradingTheme

@AndroidEntryPoint
class MarketOverviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AutotradingTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MarketOverviewScreen(
                        initialPosition = intent.getIntExtra(INITIAL_POSITION, 0),
                        onClickBack = { finish() }
                    )
                }
            }
        }
    }


    companion object {
        private const val INITIAL_POSITION = "initial_position"

        fun start(context: Context, initialPosition: Int) {
            context.startActivity(MarketOverviewActivity::class.java) {
                putExtra(INITIAL_POSITION, initialPosition)
            }
        }
    }
}