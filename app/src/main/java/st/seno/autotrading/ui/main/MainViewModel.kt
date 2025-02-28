package st.seno.autotrading.ui.main

import androidx.lifecycle.SavedStateHandle
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.data.network.socket.RxSocketClient
import st.seno.autotrading.data.network.socket.SockResponse
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.prefs.PrefsManager
import st.seno.autotrading.ui.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private var rxSocketClient: RxSocketClient = RxSocketClient.getInstance(RxSocketClient.MAIN_SOCKET)
    private val _tabIndex: MutableStateFlow<Int> = MutableStateFlow(savedStateHandle["tabIndex"] ?: 0)
    val tabIndex: StateFlow<Int> get() = _tabIndex.asStateFlow()

    fun updateTabIndex(index: Int) {
        _tabIndex.value = index
    }

    fun connectSocket() {
        var isFirstCall = true
        vmScopeJob {
            rxSocketClient
                .connect()
                .collectLatest { socketResponse: SockResponse ->
                    when(socketResponse) {
                        is SockResponse.Open -> {
                            rxSocketClient.sendMessageDaysTicker(
                                cryptos = PrefsManager.marketIdList.split(","),
                                isRealTime = true
//                                isRealTime = !isFirstCall
                            )
                        }
                        is SockResponse.Message -> {
                            val ticker = Gson().fromJson(socketResponse.data, Ticker::class.java)
                            val mutableTickersMap = tickersMap.value.toMutableMap()
                            mutableTickersMap[ticker.code] = ticker

                            val codeList = ticker.code.split("-")
                            if (codeList.size == 2) {
                                when(codeList[0]) {
                                    getString(R.string.KRW) -> {
                                        krwTickers.value = krwTickers.value.toMutableMap().apply {
                                            this[codeList[1]] = ticker
                                        }
                                    }
                                    getString(R.string.BTC) -> {
                                        btcTickers.value = btcTickers.value.toMutableMap().apply {
                                            this[codeList[1]] = ticker
                                        }
                                    }
                                    else -> {
                                        usdtTickers.value = usdtTickers.value.toMutableMap().apply {
                                            this[codeList[1]] = ticker
                                        }
                                    }
                                }
                            }

                            _tickersMap.value = mutableTickersMap.toMap()

//                            if (isFirstCall) {
//                                isFirstCall = false
//                                connectSocket()
//                            }
                        }
                        else -> { // Closing, Closed, Failure
                            isFirstCall = false
                            Timber.e(socketResponse.toString())
                        }
                    }
                }
        }
    }

    override fun onCleared() {
        RxSocketClient.releaseAllSocket()
        super.onCleared()
    }

    companion object {
        private val _tickersMap: MutableStateFlow<Map<String, Ticker>> = MutableStateFlow(mutableMapOf())
        val tickersMap: StateFlow<Map<String, Ticker>> get() = _tickersMap.asStateFlow()

        val krwTickers: MutableStateFlow<MutableMap<String, Ticker>> = MutableStateFlow(mutableMapOf())
        val btcTickers: MutableStateFlow<MutableMap<String, Ticker>> = MutableStateFlow(mutableMapOf())
        val usdtTickers: MutableStateFlow<MutableMap<String, Ticker>> = MutableStateFlow(mutableMapOf())
    }
}