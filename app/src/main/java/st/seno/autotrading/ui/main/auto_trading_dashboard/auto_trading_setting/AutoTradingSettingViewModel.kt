package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.data.network.model.isSuccess
import st.seno.autotrading.data.network.model.successData
import st.seno.autotrading.domain.MyAssetsUseCase
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.ui.base.BaseViewModel
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component.tradeModes
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AutoTradingSettingViewModel @Inject constructor(
    private val myAssetsUseCase: MyAssetsUseCase
) : BaseViewModel() {
    private val _myKrw: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val myKrw: StateFlow<Double> get() = _myKrw.asStateFlow()

    init {
        vmScopeJob { reqMyAssets() }
    }

    private suspend fun reqMyAssets() {
        myAssetsUseCase.reqMyAssets().collectLatest {
            if (it.isSuccess()) {
                it.successData()
                    .firstOrNull { asset -> asset.currency.lowercase() == getString(R.string.krw) }
                    ?.let { asset -> _myKrw.value = asset.balance.toDouble().truncateToXDecimalPlaces(x = 2.0) }
            } else {
                showMessage(message = getString(R.string.network_request_error))
                delay(1000)
                finish()
            }
        }
    }
}