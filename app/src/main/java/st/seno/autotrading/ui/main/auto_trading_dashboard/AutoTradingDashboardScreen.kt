package st.seno.autotrading.ui.main.auto_trading_dashboard

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import st.seno.autotrading.App
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.isNotNullAndNotEmpty
import st.seno.autotrading.extensions.toLocalDateTime
import st.seno.autotrading.extensions.toast
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_backtest.BackTestActivity
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.AutoTradingSettingActivity
import st.seno.autotrading.ui.common.AutoTradingNetworkErrorOverlay
import st.seno.autotrading.ui.main.auto_trading_dashboard.component.AutoTradingStatusPanel
import st.seno.autotrading.ui.main.auto_trading_dashboard.component.BackTestPanel
import st.seno.autotrading.ui.main.auto_trading_dashboard.component.TradingHistoryPanel
import st.seno.autotrading.ui.main.trading_view.TradingViewActivity
import timber.log.Timber
import java.time.format.DateTimeFormatter

@Composable
fun AutoTradingDashboardScreen() {
    val context = LocalContext.current
    val autoTradingDashboardViewModel = hiltViewModel<AutoTradingDashboardViewModel>()
    val selectedCrypto = autoTradingDashboardViewModel.selectedCryptoToTradingHistory.collectAsStateWithLifecycle().value
    val selectedDate = autoTradingDashboardViewModel.selectedDateToTradingHistory.collectAsStateWithLifecycle().value
    val tradingHistories = autoTradingDashboardViewModel.tradingHistories.collectAsStateWithLifecycle().value
    val signedChangeRate = autoTradingDashboardViewModel.signedChangeRate.collectAsStateWithLifecycle().value
    val isRunningAutoTradingService = App.autoTradingServiceStatus.collectAsStateWithLifecycle().value?.data?.isRunning ?: false
    val tradingOptions = App.autoTradingServiceStatus.collectAsStateWithLifecycle().value?.data?.tradingOptions
    val isConnectedWithLocalSocket = App.isConnectedWithLocalSocket.collectAsStateWithLifecycle().value

    LaunchedEffect(true) {
        autoTradingDashboardViewModel.message.collectLatest(context::toast)
    }

    if (isConnectedWithLocalSocket) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = FFF9FAFB)
                .verticalScroll(state = rememberScrollState())
                .nestedScroll(connection = remember {
                    object : NestedScrollConnection {
                        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                            return Offset.Zero
                        }
                    }
                })
        ) {
            35.HeightSpacer()
            BackTestPanel(onClickBackTestSetting = { BackTestActivity.start(context = context) })
            16.HeightSpacer()
            AutoTradingStatusPanel(
                isRunningAutoTradingService = isRunningAutoTradingService,
                signedChangeRate = signedChangeRate,
                tradingOptions = tradingOptions,
                onClickStopTrading = { autoTradingDashboardViewModel.reqStopAutoTrading() },
                onClickViewTrading = {
                    try {
                        tradingOptions?.let {
                            val marketId = it.marketId
                            val startDate = it.startDate?.toLocalDateTime()?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            if (marketId.isNotNullAndNotEmpty() && startDate.isNotNullAndNotEmpty()) {
                                TradingViewActivity.start(context = context, tickerCode = marketId, autoTradingStartDate = startDate)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
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
    } else {
        AutoTradingNetworkErrorOverlay(
            onClickRetry = {
                App.getInstance().connectLocalWebSocket()
            }
        )
    }
}