package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import st.seno.autotrading.service.AutoTradingService
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
                            startAutoTradingService(
                                marketId = it.selectedAutoTradingCryptoState.value,
                                quantityRatio = rationQuantities[it.quantityRatioIndexState.value].toInt(),
                                stopLoss = it.stopLossState.value.text.toInt(),
                                takeProfit = it.takeProfitState.value.text.toInt(),
                                startDate = it.startDateState.longValue,
                                endDate = it.endDateState.longValue,
                                currentTradingMode = it.currentTradingModeState.value
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
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun startAutoTradingService(
        marketId: String,
        quantityRatio: Int,
        stopLoss: Int,
        takeProfit: Int,
        startDate: Long,
        endDate: Long,
        currentTradingMode: String,
    ) {

        val serviceIntent = Intent(this@AutoTradingSettingActivity, AutoTradingService::class.java).apply {
            putExtra(KeyName.Intent.MARKET_ID, marketId)
            putExtra(KeyName.Intent.QUANTITY_RATIO, quantityRatio)
            putExtra(KeyName.Intent.STOP_LOSS, stopLoss)
            putExtra(KeyName.Intent.TAKE_PROFIT, takeProfit)
            putExtra(KeyName.Intent.START_DATE, startDate)
            putExtra(KeyName.Intent.END_DATE, endDate)
            putExtra(KeyName.Intent.CURRNET_TRADING_MODE, currentTradingMode)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(AutoTradingSettingActivity::class.java)
        }
    }
}
