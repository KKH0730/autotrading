package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import st.seno.autotrading.R
import st.seno.autotrading.extensions.startActivity
import st.seno.autotrading.extensions.toast
import st.seno.autotrading.keyname.KeyName
import st.seno.autotrading.theme.AutotradingTheme

@AndroidEntryPoint
class AutoTradingSettingActivity : ComponentActivity() {
    private val autoTradingSettingViewModel: AutoTradingSettingViewModel by viewModels<AutoTradingSettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AutotradingTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AutoTradingSettingScreen(
                        bookmarkedTickers = autoTradingSettingViewModel.bookmarkedTickers.collectAsStateWithLifecycle().value,
                        myKrw = autoTradingSettingViewModel.myKrw.collectAsStateWithLifecycle().value,
                        onClickBack = { finish() },
                        onClickStartAutoTrading = {
                            val intent = Intent().apply {
                                putExtra(KeyName.Intent.MARKET_ID, it.selectedAutoTradingCryptoState.value )
                                putExtra(KeyName.Intent.QUANTITY_RATIO, rationQuantities[it.quantityRatioIndexState.value].toInt() )
                                putExtra(KeyName.Intent.TRADING_STRATEGY, it.tradingStrategyState.value )
                                putExtra(KeyName.Intent.STOP_LOSS, it.stopLossState.value.text.toInt() )
                                putExtra(KeyName.Intent.TAKE_PROFIT, it.takeProfitState.value.text.toInt() )
                                putExtra(KeyName.Intent.CORRECTION_VALUE, it.correctionValueState.value.text.toFloat() )
                                putExtra(KeyName.Intent.START_DATE, it.startDateState.longValue )
                                putExtra(KeyName.Intent.END_DATE, it.endDateState.longValue )
                                putExtra(KeyName.Intent.CURRNET_TRADING_MODE, it.currentTradingModeState.value )
                            }
                            setResult(RESULT_OK, intent)
                            Toast.makeText(this@AutoTradingSettingActivity, getString(R.string.auto_trading_start), Toast.LENGTH_LONG).show()
                            finish()
                        }
                    )
                }
            }
        }

        startObserve()
    }

    private fun startObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { autoTradingSettingViewModel.message.collectLatest { toast(message = it) } }

                launch { autoTradingSettingViewModel.finish.collectLatest { finish() } }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(AutoTradingSettingActivity::class.java)
        }

        fun start(context: Context, launcher: ActivityResultLauncher<Intent>) {
            context.startActivity(AutoTradingSettingActivity::class.java, launcher)
        }
    }
}
