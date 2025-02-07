package st.seno.autotrading.ui.splash

import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import st.seno.autotrading.BuildConfig
import st.seno.autotrading.data.network.model.Crypto
import st.seno.autotrading.data.network.model.isSuccess
import st.seno.autotrading.data.network.model.successData
import st.seno.autotrading.domain.CandleUseCase
import st.seno.autotrading.domain.MarketUseCase
import st.seno.autotrading.prefs.PrefsManager
import st.seno.autotrading.ui.base.BaseViewModel
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val marketUseCase: MarketUseCase
) : BaseViewModel() {
    private val _startMain: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val startMain: SharedFlow<Boolean> get() = _startMain.asSharedFlow()

    init {
        vmScopeJob(coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
            vmScopeJob { _startMain.emit(false) }
        }) {
            if (!BuildConfig.DEBUG) {
                _startMain.emit(true)
                return@vmScopeJob
            }

            marketUseCase.reqMarketCrypto().collectLatest { result ->
                if (result.isSuccess()) {
                    PrefsManager.marketIdList = result.successData().joinToString(separator = ",") { it.marketId }

                    try {
                        val map: Map<String, Crypto> = result.successData().map { it.marketId to it }.toMap()
                        val json = Gson().toJson(map)
                        PrefsManager.marketAll = json
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                vmScopeJob { _startMain.emit(result.isSuccess()) }
            }
        }
    }
}