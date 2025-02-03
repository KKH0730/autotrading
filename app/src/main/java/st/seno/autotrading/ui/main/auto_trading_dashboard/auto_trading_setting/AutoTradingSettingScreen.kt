package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import st.seno.autotrading.R
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.ui.common.CommonToolbar
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component.AutoTradingSettingPanel
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component.AvailableKrwPanel

@Composable
fun AutoTradingSettingScreen(
    onClickBack: () -> Unit
) {
    val autoTradingSettingViewModel = hiltViewModel<AutoTradingSettingViewModel>()
    val selectedCrypto = autoTradingSettingViewModel.selectedAutoTradingCrypto.collectAsStateWithLifecycle(
        initialValue = "KRW-BTC",
        lifecycleOwner = LocalContext.current as AutoTradingSettingActivity,
        minActiveState = Lifecycle.State.STARTED
    ).value
    Column(
        modifier = Modifier
            .background(color = FFF9FAFB)
            .verticalScroll(rememberScrollState())
    ) {
        CommonToolbar(
            title = stringResource(R.string.auto_trading_setting_title),
            onClickBack = onClickBack
        )
        30.HeightSpacer()
        AvailableKrwPanel()
        24.HeightSpacer()
        AutoTradingSettingPanel(
            selectedCrypto = selectedCrypto,
            onClickDropdownMenuItem = {
                autoTradingSettingViewModel.updateSelectedAutoTradingCrypto(newSelectedAutoTradingCrypto = it)
            }
        )
        30.HeightSpacer()
    }
}