package st.seno.autotrading

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.data.network.response_model.ServiceSocketResponse
import st.seno.autotrading.data.network.response_model.TradingData
import st.seno.autotrading.data.network.response_model.TradingHistorySocketResponse
import st.seno.autotrading.data.network.response_model.UpbitSocketResponse
import st.seno.autotrading.data.network.socket.RxSocketClient
import st.seno.autotrading.data.network.socket.SockResponse
import st.seno.autotrading.extensions.parseOrNull
import st.seno.autotrading.util.BookmarkUtil
import timber.log.Timber

@HiltAndroidApp
class App : Application(), LifecycleObserver {

    private var rxLocalSocketClient: RxSocketClient? = RxSocketClient.getInstance(RxSocketClient.LOCAL_SOCKET)
    var appScope: CoroutineScope? = null
    
    companion object {
        private lateinit var instance: App

        private val _tickersMap: MutableStateFlow<Map<String, Ticker>> = MutableStateFlow(mutableMapOf())
        val tickersMap: StateFlow<Map<String, Ticker>> get() = _tickersMap.asStateFlow()

        private val _autoTradingServiceStatus: MutableStateFlow<ServiceSocketResponse?> = MutableStateFlow(null)
        val autoTradingServiceStatus: StateFlow<ServiceSocketResponse?> get() = _autoTradingServiceStatus.asStateFlow()

        private val _autoTradingHistory: MutableStateFlow<List<TradingData>> = MutableStateFlow(listOf())
        val autoTradingHistory: StateFlow<List<TradingData>> get() = _autoTradingHistory.asStateFlow()

        private val _isConnectedWithLocalSocket: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val isConnectedWithLocalSocket: StateFlow<Boolean> get() = _isConnectedWithLocalSocket.asStateFlow()

        val krwTickers: MutableStateFlow<MutableMap<String, Ticker>> = MutableStateFlow(mutableMapOf())
        val btcTickers: MutableStateFlow<MutableMap<String, Ticker>> = MutableStateFlow(mutableMapOf())
        val usdtTickers: MutableStateFlow<MutableMap<String, Ticker>> = MutableStateFlow(mutableMapOf())

        fun getInstance(): App = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        initTimber()
        initPrefs()
        initBookmark()

        ProcessLifecycleOwner.get().lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onStart(owner: LifecycleOwner) {
                    super.onStart(owner)
                    appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

                    connectLocalWebSocket()
                }

                override fun onStop(owner: LifecycleOwner) {
                    super.onStop(owner)
                    appScope?.cancel()
                    appScope = null

                    RxSocketClient.releaseAllSocket()
                }
            }
        )
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initPrefs() {
        Prefs.Builder()
            .setContext(this)
            .setMode(MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }

    private fun initBookmark() {
        BookmarkUtil.init()
    }

    fun connectLocalWebSocket() {
        if (appScope == null) {
            appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        }

        if (rxLocalSocketClient == null) {
            rxLocalSocketClient = RxSocketClient.getInstance(RxSocketClient.LOCAL_SOCKET)
        }

        if (rxLocalSocketClient?.isConnected == true) {
            return
        }

        appScope?.launch {
            rxLocalSocketClient
                ?.connect(url = "ws://110.9.68.93:8080/ws/trading", userKey = "1")
                ?.collectLatest { socketResponse: SockResponse ->
                    when(socketResponse) {
                        is SockResponse.Open -> {
                            _isConnectedWithLocalSocket.value = true
                        }
                        is SockResponse.Message -> {
                            val json = JSONObject(socketResponse.data)
                            val mode = json.get("mode") as? String

                            when(mode) {
                                "operation" -> {
                                    val serviceSocketResponse = socketResponse.data.parseOrNull<ServiceSocketResponse>()
                                    _autoTradingServiceStatus.value = serviceSocketResponse
                                }
                                "history" -> {
                                    val tradingHistorySocketResponse = socketResponse.data.parseOrNull<TradingHistorySocketResponse>()
                                    _autoTradingHistory.value = tradingHistorySocketResponse?.data ?: emptyList()

                                }
                                "upbit" -> {
                                    val upbitSocketResponse = socketResponse.data.parseOrNull<UpbitSocketResponse>()
                                    if (upbitSocketResponse == null) return@collectLatest

                                    val ticker = upbitSocketResponse.data
                                    val mutableTickersMap = tickersMap.value.toMutableMap()
                                    mutableTickersMap[ticker.code] = ticker

                                    val codeList = ticker.code.split("-")
                                    if (codeList.size == 2) {
                                        when(codeList[0]) {
                                            st.seno.autotrading.extensions.getString(R.string.KRW) -> {
                                                krwTickers.value = krwTickers.value.toMutableMap().apply {
                                                    this[codeList[1]] = ticker
                                                }
                                            }
                                            st.seno.autotrading.extensions.getString(R.string.BTC) -> {
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
                                }
                                else -> {}
                            }
                            _isConnectedWithLocalSocket.value = true
                        }
                        is SockResponse.Closing -> {
                            FirebaseCrashlytics.getInstance().recordException(Exception("Socket Closing -> code: ${socketResponse.code}, reason : ${socketResponse.reason}"))
                            _isConnectedWithLocalSocket.value = false
                        }
                        is SockResponse.Closed -> {
                            FirebaseCrashlytics.getInstance().recordException(Exception("Socket Closed -> code: ${socketResponse.code}, reason : ${socketResponse.reason}"))
                            _isConnectedWithLocalSocket.value = false
                        }
                        is SockResponse.Failure -> {
                            FirebaseCrashlytics.getInstance().recordException(Exception("Socket Failure -> t.message: ${socketResponse.t.message}, cause: ${socketResponse.t.cause}"))
                            _isConnectedWithLocalSocket.value = false
                        }
                        is SockResponse.Reconnect -> {
                            _isConnectedWithLocalSocket.value = false
                        }
                        is SockResponse.TerminationState -> {
                            _isConnectedWithLocalSocket.value = false
                            rxLocalSocketClient?.release()
                            rxLocalSocketClient = null
                            RxSocketClient.releaseSocket(RxSocketClient.LOCAL_SOCKET)
                            appScope?.cancel()
                            appScope = null
                        }
                    }
                }
        }
    }
}