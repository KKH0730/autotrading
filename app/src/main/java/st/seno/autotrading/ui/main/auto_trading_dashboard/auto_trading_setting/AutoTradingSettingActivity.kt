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
                            autoTradingSettingViewModel.startAutoTrading(
                                marketId = it.selectedAutoTradingCryptoState.value,
                                quantityRatio = rationQuantities[it.quantityRatioIndexState.value].toInt(),
                                stopLoss = it.stopLossState.value.text.toInt(),
                                takeProfit = it.takeProfitState.value.text.toInt(),
                                correctionValue = it.correctionValueState.value.text.toFloat(),
                                startDate = it.startDateState.longValue,
                                endDateTime = it.endDateState.longValue,
                                tradingStrategy = it.tradingStrategyState.value
                            )
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

                launch {
                    autoTradingSettingViewModel.startAutoTrading.collectLatest { tradingOptions ->
                        toast(message = getString(R.string.auto_trading_start))
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(AutoTradingSettingActivity::class.java)
        }
    }
}
