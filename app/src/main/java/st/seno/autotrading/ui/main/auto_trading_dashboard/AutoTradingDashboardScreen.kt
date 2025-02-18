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
import androidx.compose.runtime.LaunchedEffect
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
import st.seno.autotrading.service.AutoTradingService
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.ui.main.MainActivity
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_backtest.BackTestActivity
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.AutoTradingSettingActivity
import st.seno.autotrading.ui.main.auto_trading_dashboard.component.AutoTradingStatusPanel
import st.seno.autotrading.ui.main.auto_trading_dashboard.component.BackTestPanel
import st.seno.autotrading.ui.main.auto_trading_dashboard.component.TradingHistoryPanel
import st.seno.autotrading.ui.main.trading_view.TradingViewActivity
import timber.log.Timber
import java.util.Calendar

@Composable
fun AutoTradingDashboardScreen() {
    val context = LocalContext.current
    val autoTradingDashboardViewModel = hiltViewModel<AutoTradingDashboardViewModel>()
    val isRunningAutoTradingService = AutoTradingService.isRunningAutoTradingService.collectAsStateWithLifecycle().value
    val selectedCrypto = autoTradingDashboardViewModel.selectedCryptoToTradingHistory.collectAsStateWithLifecycle().value
    val selectedDate =  autoTradingDashboardViewModel.selectedDateToTradingHistory.collectAsStateWithLifecycle().value
    val tradingHistories = autoTradingDashboardViewModel.tradingHistories.collectAsStateWithLifecycle().value
    val signedChangeRate = autoTradingDashboardViewModel.signedChangeRate.collectAsStateWithLifecycle().value

    BackHandler {  }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {

        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                AutoTradingService.tradingStartDate.takeIf { it.isNotEmpty() }?.let {
                    autoTradingDashboardViewModel.reqTradingData(startDate = it)
                }
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll

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
            onClickViewTrading = { AutoTradingService.tradingMarketId.takeIf { it.isNotEmpty() }?.let { TradingViewActivity.start(context = context, tickerCode = it) } } ,
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
}

private fun stopService(context: Context) {
    val serviceIntent = Intent(context, AutoTradingService::class.java)
    context.stopService(serviceIntent)
}