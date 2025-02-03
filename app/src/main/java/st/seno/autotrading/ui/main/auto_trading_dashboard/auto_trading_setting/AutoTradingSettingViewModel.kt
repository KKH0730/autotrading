package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import st.seno.autotrading.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AutoTradingSettingViewModel @Inject constructor() : BaseViewModel() {
    private val _selectedAutoTradingCrypto: MutableStateFlow<String> = MutableStateFlow("KRW-BTC")
    val selectedAutoTradingCrypto: StateFlow<String> get() = _selectedAutoTradingCrypto.asStateFlow()

    fun updateSelectedAutoTradingCrypto(newSelectedAutoTradingCrypto: String) {
        _selectedAutoTradingCrypto.value = newSelectedAutoTradingCrypto
    }
}