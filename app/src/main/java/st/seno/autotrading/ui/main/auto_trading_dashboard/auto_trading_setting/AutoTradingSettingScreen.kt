package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting

import android.annotation.SuppressLint
import android.icu.util.Calendar
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.extensions.update
import st.seno.autotrading.theme.FF2563EB
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.common.CommonToolbar
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component.AutoTradingSettingPanel
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component.AvailableKrwPanel

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AutoTradingSettingScreen(
    bookmarkedTickers: List<String>,
    myKrw: Double,
    onClickBack: () -> Unit,
    onClickStartAutoTrading: (AutoTradingSettingState) -> Unit
) {
    val todayCal = Calendar.getInstance()
    val endCal = Calendar.getInstance().apply {
        set(Calendar.MONTH , todayCal.get(Calendar.MONTH) + 1)
    }

    val autoTradingSettingState = rememberAutoTradingSettingState(
        startDateState = mutableLongStateOf(todayCal.timeInMillis),
        endDateState = mutableLongStateOf(endCal.timeInMillis),
        tradeDateState = mutableStateOf(TradeDate(isShowDatePicker = false, tradeDateType = TradeDateType.START, selectedDate = todayCal.timeInMillis))
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = autoTradingSettingState.snackbarHostState) { data ->
                Snackbar {
                    Text(
                        text = data.visuals.message,
                        style = TextStyle(
                            fontSize = 14.textDp,
                            fontWeight = FontWeight.Medium,
                            color = FFFFFFFF
                        )
                    )
                }
            }
        }
    ) { _ ->
        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            Column(modifier = Modifier.fillMaxSize()) {
                CommonToolbar(
                    title = stringResource(R.string.auto_trading_setting_title),
                    onClickBack = onClickBack
                )
                Column(
                    modifier = Modifier
                        .background(color = FFF9FAFB)
                        .verticalScroll(rememberScrollState())
                ) {
                    30.HeightSpacer()
                    AvailableKrwPanel(myKrw = myKrw)
                    24.HeightSpacer()
                    AutoTradingSettingPanel(
                        selectedAutoTradingCrypto = autoTradingSettingState.selectedAutoTradingCryptoState.value,
                        isExpandCryptoDropDownMenu = autoTradingSettingState.expandCryptoDropDownMenuState.value,
                        bookmarkedTickers = bookmarkedTickers,
                        quantityRatioIndex = autoTradingSettingState.quantityRatioIndexState.value,
                        currentTradingMode = autoTradingSettingState.currentTradingModeState.value,
                        stopLossValue = autoTradingSettingState.stopLossState.value,
                        takeProfitValue = autoTradingSettingState.takeProfitState.value,
                        correctionValue = autoTradingSettingState.correctionValueState.value,
                        startDateValue = autoTradingSettingState.startDateState.longValue,
                        endDateValue = autoTradingSettingState.endDateState.longValue,
                        tradeDateValue = autoTradingSettingState.tradeDateState.value,
                        onClickCryptoText = { autoTradingSettingState.expandCryptoDropDownMenuState.update(value = !autoTradingSettingState.expandCryptoDropDownMenuState.value) },
                        onClickCryptoDropdownMenu = { autoTradingSettingState.expandCryptoDropDownMenuState.update(value = it) },
                        onClickCryptoDropdownMenuItem = { autoTradingSettingState.selectedAutoTradingCryptoState.update(value = it) },
                        onClickQuantityRatio = { autoTradingSettingState.quantityRatioIndexState.update(value = it) },
                        onClickTradingMode = { autoTradingSettingState.currentTradingModeState.update(value = it) },
                        onStopLossChanged = { autoTradingSettingState.stopLossState.update(value = it) },
                        onTakeProfitChanged = { autoTradingSettingState.takeProfitState.update(value = it) },
                        onCorrectionValueChanged = { autoTradingSettingState.correctionValueState.update(value = it) },
                        onClickStartDatePicker = { autoTradingSettingState.tradeDateState.update(value = TradeDate(isShowDatePicker = true, tradeDateType = TradeDateType.START, selectedDate = it)) },
                        onClickEndDatePicker = { autoTradingSettingState.tradeDateState.update(value = TradeDate(isShowDatePicker = true, tradeDateType = TradeDateType.END, selectedDate = it)) },
                        onChangeDate = { tradeDate ->
                            if (tradeDate.tradeDateType.name == TradeDateType.START.name && tradeDate.selectedDate >= autoTradingSettingState.endDateState.longValue) {
                                autoTradingSettingState.showSnackBar(text = getString(R.string.auto_trading_date_alert))
                                autoTradingSettingState.tradeDateState.update(value = TradeDate(isShowDatePicker = false, tradeDateType = TradeDateType.START, selectedDate = autoTradingSettingState.startDateState.longValue))
                                return@AutoTradingSettingPanel
                            } else if (tradeDate.tradeDateType.name == TradeDateType.END.name && tradeDate.selectedDate <= autoTradingSettingState.startDateState.longValue) {
                                autoTradingSettingState.showSnackBar(text = getString(R.string.auto_trading_date_alert))
                                autoTradingSettingState.tradeDateState.update(value = TradeDate(isShowDatePicker = false, tradeDateType = TradeDateType.END, selectedDate = autoTradingSettingState.endDateState.longValue))
                                return@AutoTradingSettingPanel
                            } else {
                                if (tradeDate.tradeDateType.name == TradeDateType.START.name) {
                                    autoTradingSettingState.startDateState.update(value = tradeDate.selectedDate)
                                } else {
                                    autoTradingSettingState.endDateState.update(value = tradeDate.selectedDate)
                                }
                                autoTradingSettingState.tradeDateState.update(value = tradeDate)
                            }
                        }
                    )
                    30.HeightSpacer()
                    StartAutoTradingButton(onClickStartAutoTrading = {
                        if (myKrw < 5000.0) {
                            autoTradingSettingState.showSnackBar(text = getString(R.string.auto_trading_not_enough))
                        } else {
                            onClickStartAutoTrading.invoke(autoTradingSettingState)
                        }
                    })
                    30.HeightSpacer()
                }
            }
        }
    }
}

@Composable
fun StartAutoTradingButton(
    onClickStartAutoTrading: () -> Unit
) {
    Button(
        onClick = onClickStartAutoTrading,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = FF2563EB,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 40.dp)
            .padding(horizontal = 24.dp)
    ) {
        Text(
            stringResource(R.string.start),
            style = TextStyle(
                fontSize = 14.textDp,
                fontWeight = FontWeight.Medium,
                color = FFFFFFFF
            ),
            overflow = TextOverflow.Ellipsis,
        )
    }
}