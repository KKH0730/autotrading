package st.seno.autotrading.ui.main.auto_trading_dashboard

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.toDate
import st.seno.autotrading.prefs.PrefsManager
import st.seno.autotrading.service.AutoTradingService
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.ui.main.MainActivity
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_backtest.BackTestActivity
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.AutoTradingSettingActivity
import st.seno.autotrading.ui.main.auto_trading_dashboard.component.AutoTradingStatusPanel
import st.seno.autotrading.ui.main.auto_trading_dashboard.component.BackTestPanel
import st.seno.autotrading.ui.main.auto_trading_dashboard.component.TradingHistoryPanel
import st.seno.autotrading.ui.main.trading_view.TradingViewActivity
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AutoTradingDashboardScreen() {
    val context = LocalContext.current
    val autoTradingDashboardViewModel = hiltViewModel<AutoTradingDashboardViewModel>()
    val isRunningAutoTradingService = AutoTradingService.isRunningAutoTradingService.collectAsStateWithLifecycle().value
    val selectedCrypto = autoTradingDashboardViewModel.selectedCryptoToTradingHistory.collectAsStateWithLifecycle().value
    val selectedDate =  autoTradingDashboardViewModel.selectedDateToTradingHistory.collectAsStateWithLifecycle().value
    val tradingHistories = autoTradingDashboardViewModel.tradingHistories.collectAsStateWithLifecycle().value
    val signedChangeRate = autoTradingDashboardViewModel.signedChangeRate.collectAsStateWithLifecycle().value
    val lifecycleOwner = LocalLifecycleOwner.current

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return Offset.Zero
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = FFF9FAFB)
            .verticalScroll(state = rememberScrollState())
            .nestedScroll(connection = nestedScrollConnection)
    ) {
        35.HeightSpacer()
        BackTestPanel(onClickBackTestSetting = { BackTestActivity.start(context = context) })
        16.HeightSpacer()
        AutoTradingStatusPanel(
            isRunningAutoTradingService = isRunningAutoTradingService,
            signedChangeRate = signedChangeRate,
            onClickStopTrading = { stopService(context = context as MainActivity) },
            onClickViewTrading = {
                try {
                    val marketId = PrefsManager.AutoTrading.marketId
                    val startDate = PrefsManager.AutoTrading.startDate.toDate("yyyy-MM-dd")
                    if (marketId.isNotEmpty() && startDate.isNotEmpty()) {
                        TradingViewActivity.start(context = context, tickerCode = marketId, autoTradingStartDate = startDate)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } ,
            onClickStartAutoTrading = { AutoTradingSettingActivity.start(context = context) }
        )
        16.HeightSpacer()
        TradingHistoryPanel(
            selectedCrypto = selectedCrypto,
            selectedDate = selectedDate,
            tradingHistories = tradingHistories,
            onClickDropdownMenuItem = { autoTradingDashboardViewModel.updateTradeHistorySelectedCrypto(newSelectedCrypto = it) },
            onSelectedDate = { autoTradingDashboardViewModel.updateTradeHistorySelectedDate(newDate = it) },
            onClickApplyFilter = { autoTradingDashboardViewModel.reqClosedOrders() }
        )
        24.HeightSpacer()
    }

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                try {
                    PrefsManager.AutoTrading.startDate.toDate("yyyy-MM-dd").takeIf { it.isNotEmpty() }?.let {
                        autoTradingDashboardViewModel.reqTradingData(startDate = it)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

private fun stopService(context: Context) {
    val serviceIntent = Intent(context, AutoTradingService::class.java)
    context.stopService(serviceIntent)
}