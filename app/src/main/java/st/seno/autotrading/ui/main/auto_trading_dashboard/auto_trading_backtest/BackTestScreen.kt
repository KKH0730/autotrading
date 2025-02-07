package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_backtest

import android.annotation.SuppressLint
import android.graphics.Paint.Align
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import st.seno.autotrading.R
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.extensions.isEmpty
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.extensions.update
import st.seno.autotrading.theme.FF2563EB
import st.seno.autotrading.theme.FF374151
import st.seno.autotrading.theme.FFF3F4F6
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.common.CommonToolbar
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_backtest.component.BackTestResultPanel
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_backtest.component.BackTestSettingsPanel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BacktestScreen(
    onClickBack: () -> Unit
) {

    val context = LocalContext.current
    val backTestViewModel = hiltViewModel<BackTestViewModel>()
    val backTestState = rememberBackTestState()
    val backTestResult = backTestViewModel.backTestResult.collectAsStateWithLifecycle(
        initialValue = null,
        lifecycleOwner = context as BackTestActivity,
        minActiveState = Lifecycle.State.STARTED,
    ).value
    val isLoading = backTestViewModel.isLoading.collectAsStateWithLifecycle().value

    if (backTestResult != null) {
        backTestState.expand()
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.analyze)
    )

    LaunchedEffect(key1 = true, key2 = backTestResult) {
        launch {
            backTestViewModel.messageSnackbar.collectLatest {
                backTestState.showSnackBar(text = it)
            }
        }
    }

    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        BottomSheetScaffold(
            snackbarHost = {
                SnackbarHost(hostState = backTestState.snackbarHostState) { data ->
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
            },
            scaffoldState = backTestState.bottomSheetScaffoldState,
            sheetPeekHeight = 0.dp,
            sheetShadowElevation = 12.dp,
            sheetShape = RoundedCornerShape(size = 50.dp),
            sheetContainerColor = FFF9FAFB,
            sheetSwipeEnabled = true,
            sheetContent = { backTestResult?.let { BackTestResultPanel(backTestResult = it, onClickConfirm = { backTestState.partialExpand() }) } }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    CommonToolbar(
                        title = stringResource(R.string.auto_trading_back_test_setting),
                        onClickBack = onClickBack
                    )
                    Column(
                        modifier = Modifier
                            .background(color = FFF9FAFB)
                            .verticalScroll(rememberScrollState())
                    ) {
                        30.HeightSpacer()
                        BackTestSettingsPanel(
                            selectedAutoTradingCrypto = backTestState.selectedBackTestCryptoState.value,
                            isExpandCryptoDropDownMenu = backTestState.expandCryptoDropDownMenuState.value,
                            initialInvestment = backTestState.initialInvestment.value,
                            sampleCountValue = backTestState.sampleCountState.value,
                            stopLossValue = backTestState.stopLossState.value,
                            takeProfitValue = backTestState.takeProfitState.value,
                            correctionValue = backTestState.correctionValueState.value,
                            onClickCryptoText = { backTestState.expandCryptoDropDownMenuState.update(value = !backTestState.expandCryptoDropDownMenuState.value) },
                            onClickCryptoDropdownMenu = { backTestState.expandCryptoDropDownMenuState.update(value = it) },
                            onClickCryptoDropdownMenuItem = { backTestState.selectedBackTestCryptoState.update(value = it) },
                            onSampleCountChanged = { backTestState.sampleCountState.update(value = it) },
                            onStopLossChanged = { backTestState.stopLossState.update(value = it) },
                            onTakeProfitChanged = { backTestState.takeProfitState.update(value = it) },
                            onInitialInvestmentChanged = { backTestState.initialInvestment.update(value = it) },
                            onCorrectionValueChanged = { backTestState.correctionValueState.update(value = it) }
                        )
                        30.HeightSpacer()
                        StartBackTestButton(
                            onClickStartBackTest = {
                                if (backTestState.initialInvestment.value.text == "0" || backTestState.initialInvestment.value.isEmpty()) {
                                    backTestState.showSnackBar(text = getString(R.string.auto_trading_back_test_check_initial_investment))
                                    return@StartBackTestButton
                                }

                                if (backTestState.sampleCountState.value.isEmpty()) {
                                    backTestState.showSnackBar(text = getString(R.string.auto_trading_back_test_check_sample_count))
                                    return@StartBackTestButton
                                }

                                if (backTestState.stopLossState.value.isEmpty()) {
                                    backTestState.showSnackBar(text = getString(R.string.auto_trading_back_test_check_stop_loss))
                                    return@StartBackTestButton
                                }

                                if (backTestState.takeProfitState.value.isEmpty()) {
                                    backTestState.showSnackBar(text = getString(R.string.auto_trading_back_test_check_take_profit))
                                    return@StartBackTestButton
                                }

                                if (backTestState.correctionValueState.value.isEmpty()) {
                                    backTestState.showSnackBar(text = getString(R.string.auto_trading_back_test_check_correction_value))
                                    return@StartBackTestButton
                                }

                                backTestViewModel.reqBackTestData(
                                    marketId = backTestState.selectedBackTestCryptoState.value,
                                    initialInvestment = backTestState.initialInvestment.value.text.toDouble(),
                                    sampleCount = backTestState.sampleCountState.value.text.toInt(),
                                    stopLoss = backTestState.stopLossState.value.text.toInt(),
                                    takeProfit = backTestState.takeProfitState.value.text.toInt(),
                                    correctionValue = backTestState.correctionValueState.value.text.toDouble()
                                )
                            }
                        )
                        30.HeightSpacer()
                    }
                }

                androidx.compose.material.Card(
                    shape = RoundedCornerShape(size = 16.dp),
                    elevation = 2.dp,
                    modifier = Modifier
                        .align(alignment = Alignment.Center),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LottieAnimation(
                            composition = composition,
                            iterations = Int.MAX_VALUE,
                            speed = 2f,
                            modifier = Modifier
                                .height(height = 150.dp)
                                .width(width = 300.dp)
                        )
                        Text(
                            stringResource(R.string.auto_trading_back_test_progress),
                            style = TextStyle(
                                fontSize = 14.textDp,
                                fontWeight = FontWeight.Medium,
                                color = FF374151
                            )
                        )
                        24.HeightSpacer()
                    }
                }

                if (isLoading) {
                    androidx.compose.material.Card(
                        shape = RoundedCornerShape(size = 16.dp),
                        elevation = 2.dp,
                        modifier = Modifier
                            .height(height = 120.dp)
                            .width(width = 200.dp)
                            .align(alignment = Alignment.Center),
                    ) {
                        LottieAnimation(
                            composition = composition,
                            iterations = Int.MAX_VALUE,
                            speed = 2f,
                            modifier = Modifier
                                .height(height = 120.dp)
                                .width(width = 200.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StartBackTestButton(
    onClickStartBackTest: () -> Unit
) {
    Button(
        onClick = onClickStartBackTest,
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