package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_backtest

import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class BackTestState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val selectedBackTestCryptoState: MutableState<String>,
    val expandCryptoDropDownMenuState: MutableState<Boolean>,
    val initialInvestment: MutableState<TextFieldValue>,
    val sampleCountState: MutableState<TextFieldValue>,
    val stopLossState: MutableState<TextFieldValue>,
    val takeProfitState: MutableState<TextFieldValue>,
    val correctionValueState: MutableState<TextFieldValue>,
    val bottomSheetScaffoldState: BottomSheetScaffoldState,
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

    @OptIn(ExperimentalMaterial3Api::class)
    fun expand() {
        coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.expand() }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun partialExpand() {
        coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.partialExpand() }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun show() {
        coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.show() }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun hide() {
        coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.hide() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberBackTestState(
    selectedBackTestCryptoState: MutableState<String> = mutableStateOf("KRW-BTC"),
    expandCryptoDropDownMenuState: MutableState<Boolean> = mutableStateOf(false),
    initialInvestment: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(text = "0", selection = TextRange(index = "0".length))),
    sampleCountState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(text = "200", selection = TextRange(index = "200".length))),
    stopLossState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(text = "0", selection = TextRange(index = "0".length))),
    takeProfitState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(text = "0", selection = TextRange(index = "0".length))),
    correctionValueState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(text = "0.0", selection = TextRange(index = "0.0".length))),
    bottomSheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) = remember() {
    BackTestState(
        selectedBackTestCryptoState = selectedBackTestCryptoState,
        expandCryptoDropDownMenuState = expandCryptoDropDownMenuState,
        initialInvestment = initialInvestment,
        sampleCountState = sampleCountState,
        stopLossState = stopLossState,
        takeProfitState = takeProfitState,
        correctionValueState = correctionValueState,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState
    )
}