package st.seno.autotrading.ui.main.auto_trading_dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import st.seno.autotrading.keyname.KeyName
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
import timber.log.Timber

@Composable
fun AutoTradingDashboardScreen() {
    val context = LocalContext.current
    val autoTradingDashboardViewModel = hiltViewModel<AutoTradingDashboardViewModel>()
    val selectedCrypto = autoTradingDashboardViewModel.selectedCryptoToTradingHistory.collectAsStateWithLifecycle().value
    val selectedDate = autoTradingDashboardViewModel.selectedDateToTradingHistory.collectAsStateWithLifecycle().value
    val tradingHistories = autoTradingDashboardViewModel.tradingHistories.collectAsStateWithLifecycle().value
    val signedChangeRate = autoTradingDashboardViewModel.signedChangeRate.collectAsStateWithLifecycle().value
    val lifecycleOwner = LocalLifecycleOwner.current

    var isRunningAutoTradingService by remember { mutableStateOf(PrefsManager.AutoTrading.isRunningTradingService) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intent = result.data
        if (result.resultCode == Activity.RESULT_OK && intent != null) {
            PrefsManager.AutoTrading.apply {
                marketId = intent.getStringExtra(KeyName.Intent.MARKET_ID) ?: ""
                quantityRatio = intent.getIntExtra(KeyName.Intent.QUANTITY_RATIO, 0)
                tradingStrategy = intent.getStringExtra(KeyName.Intent.TRADING_STRATEGY) ?: ""
                stopLoss = intent.getIntExtra(KeyName.Intent.STOP_LOSS, 0)
                takeProfit = intent.getIntExtra(KeyName.Intent.TAKE_PROFIT, 0)
                correctionValue = intent.getFloatExtra(KeyName.Intent.CORRECTION_VALUE, 0f)
                startDate = intent.getLongExtra(KeyName.Intent.START_DATE, 0L)
                endDate = intent.getLongExtra(KeyName.Intent.END_DATE, 0L)
                tradingMode = intent.getStringExtra(KeyName.Intent.CURRNET_TRADING_MODE) ?: ""
            }
            isRunningAutoTradingService = true

            startAutoTradingService(
                context = context as MainActivity,
                marketId = intent.getStringExtra(KeyName.Intent.MARKET_ID) ?: "",
                quantityRatio = intent.getIntExtra(KeyName.Intent.QUANTITY_RATIO, 0),
                tradingStrategy = intent.getStringExtra(KeyName.Intent.TRADING_STRATEGY) ?: "",
                stopLoss = intent.getIntExtra(KeyName.Intent.STOP_LOSS, 0),
                takeProfit = intent.getIntExtra(KeyName.Intent.TAKE_PROFIT, 0),
                correctionValue = intent.getFloatExtra(KeyName.Intent.CORRECTION_VALUE, 0f),
                startDate = intent.getLongExtra(KeyName.Intent.START_DATE, 0L),
                endDate = intent.getLongExtra(KeyName.Intent.END_DATE, 0L),
                currentTradingMode = intent.getStringExtra(KeyName.Intent.CURRNET_TRADING_MODE) ?: ""
            )
        }
    }
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
            onClickStopTrading = {
                PrefsManager.AutoTrading.apply {
                    marketId = ""
                    quantityRatio = 0
                    tradingStrategy = ""
                    stopLoss = 0
                    stopLossPrice = ""
                    takeProfit = 0
                    takeProfitPrice = ""
                    correctionValue = 0f
                    startDate = 0L
                    endDate = 0L
                    tradingMode = ""
                    isRunningTradingService = false
                }
                PrefsManager.Data.apply {
                    isSkipBid = false
                    tradePrice = 0.0
                    bidOrder = null
                    askOrder = null
                }
                isRunningAutoTradingService = false

                stopService(context = context as MainActivity)
            },
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
            },
            onClickStartAutoTrading = { AutoTradingSettingActivity.start(context = context, launcher = launcher) }
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

@SuppressLint("ObsoleteSdkInt")
private fun startAutoTradingService(
    context: Context,
    marketId: String,
    quantityRatio: Int,
    tradingStrategy: String,
    stopLoss: Int,
    takeProfit: Int,
    correctionValue: Float,
    startDate: Long,
    endDate: Long,
    currentTradingMode: String
) {
    val serviceIntent = Intent(context, AutoTradingService::class.java).apply {
        putExtra(KeyName.Intent.MARKET_ID, marketId)
        putExtra(KeyName.Intent.QUANTITY_RATIO, quantityRatio)
        putExtra(KeyName.Intent.TRADING_STRATEGY, tradingStrategy)
        putExtra(KeyName.Intent.STOP_LOSS, stopLoss)
        putExtra(KeyName.Intent.TAKE_PROFIT, takeProfit)
        putExtra(KeyName.Intent.CORRECTION_VALUE, correctionValue)
        putExtra(KeyName.Intent.START_DATE, startDate)
        putExtra(KeyName.Intent.END_DATE, endDate)
        putExtra(KeyName.Intent.CURRNET_TRADING_MODE, currentTradingMode)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(serviceIntent)
    } else {
        context.startService(serviceIntent)
    }
}

private fun stopService(context: Context) {
    val serviceIntent = Intent(context, AutoTradingService::class.java)
    context.stopService(serviceIntent)
}