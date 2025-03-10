package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import st.seno.autotrading.R
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component.tradeModes
import timber.log.Timber

val rationQuantities = listOf("5", "10", "25", "50", "75", "100")

enum class TradeDateType {
    START,
    END
}

class TradeDate(
    val isShowDatePicker: Boolean,
    val tradeDateType: TradeDateType,
    val selectedDate: Long
)

class AutoTradingSettingState(
    val selectedAutoTradingCryptoState: MutableState<String>,
    val currentTradingModeState: MutableState<String>,
    val expandCryptoDropDownMenuState: MutableState<Boolean>,
    val quantityRatioIndexState: MutableState<Int>,
    val tradingStrategyState: MutableState<String>,
    val stopLossState: MutableState<TextFieldValue>,
    val takeProfitState: MutableState<TextFieldValue>,
    val correctionValueState: MutableState<TextFieldValue>,
    val startDateState: MutableLongState,
    val endDateState: MutableLongState,
    val tradeDateState: MutableState<TradeDate>,
    val coroutineScope: CoroutineScope,
    val snackbarHostState: SnackbarHostState
) {
    fun showSnackBar(text: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = text,
                duration = SnackbarDuration.Short
            )
        }
    }
}

@Composable
fun rememberAutoTradingSettingState(
    selectedAutoTradingCryptoState: MutableState<String> = mutableStateOf("KRW-BTC"),
    currentTradingModeState: MutableState<String> = mutableStateOf(tradeModes[0]),
    expandCryptoDropDownMenuState: MutableState<Boolean> = mutableStateOf(false),
    quantityRatioIndexState: MutableState<Int> = mutableIntStateOf(0),
    tradingStrategyState: MutableState<String> = mutableStateOf(getString(R.string.auto_trading_strategy_menu_1)),
    stopLossState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(text = "0", selection = TextRange(index = "0".length))),
    takeProfitState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(text = "0", selection = TextRange(index = "0".length))),
    correctionValueState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(text = "0.0", selection = TextRange(index = "0.0".length))),
    startDateState: MutableLongState,
    endDateState: MutableLongState,
    tradeDateState: MutableState<TradeDate>,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) = remember() {
    AutoTradingSettingState(
        selectedAutoTradingCryptoState = selectedAutoTradingCryptoState,
        currentTradingModeState = currentTradingModeState,
        expandCryptoDropDownMenuState = expandCryptoDropDownMenuState,
        quantityRatioIndexState = quantityRatioIndexState,
        tradingStrategyState = tradingStrategyState,
        stopLossState = stopLossState,
        takeProfitState = takeProfitState,
        correctionValueState = correctionValueState,
        startDateState = startDateState,
        endDateState = endDateState,
        tradeDateState = tradeDateState,
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState
    )
}