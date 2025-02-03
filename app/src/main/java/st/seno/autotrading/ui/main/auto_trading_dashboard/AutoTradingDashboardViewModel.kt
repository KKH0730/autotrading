package st.seno.autotrading.ui.main.auto_trading_dashboard

import android.annotation.SuppressLint
import android.icu.util.Calendar
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pixplicity.easyprefs.library.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import st.seno.autotrading.data.network.ItemPagingSource
import st.seno.autotrading.data.network.model.ClosedOrder
import st.seno.autotrading.data.network.model.isSuccess
import st.seno.autotrading.data.network.model.successData
import st.seno.autotrading.domain.OrderUseCase
import st.seno.autotrading.ui.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AutoTradingDashboardViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : BaseViewModel() {

    private val _selectedCryptoToTradingHistory: MutableStateFlow<String> = MutableStateFlow("KRW-BTC")
    val selectedCryptoToTradingHistory: StateFlow<String> get() = _selectedCryptoToTradingHistory.asStateFlow()

    @SuppressLint("SimpleDateFormat")
    private val _selectedDateToTradingHistory: MutableStateFlow<Long> = MutableStateFlow(Calendar.getInstance().timeInMillis)
    val selectedDateToTradingHistory: StateFlow<Long> get() = _selectedDateToTradingHistory.asStateFlow()

    private val _tradingHistories: MutableStateFlow<List<ClosedOrder>?> = MutableStateFlow(null)
    val tradingHistories: StateFlow<List<ClosedOrder>?> get() = _tradingHistories.asStateFlow()


    fun updateTradeHistorySelectedCrypto(newSelectedCrypto: String) {
        _selectedCryptoToTradingHistory.value = newSelectedCrypto
    }

    fun updateTradeHistorySelectedDate(newDate: Long) {
        _selectedDateToTradingHistory.value = newDate
    }

    /**
     * startTime은 정의하지 않고 endTime만 정의함
     * endTime만 정의할 경우 최대 7일전까지 조회함
     *
     */
    fun reqClosedOrders() {
        val timeInMillis = _selectedDateToTradingHistory.value
        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }

        vmScopeJob {
            orderUseCase.reqClosedOrders(
                marketId = _selectedCryptoToTradingHistory.value,
                states = arrayOf("done, cancel"),
                startTime = null,
                endTime = calendar.timeInMillis.toString(),
                limit = 100
            ).collectLatest { result ->
                if (result.isSuccess()) {
                    Timber.e(result.successData().toString())
                    _tradingHistories.value = result.successData()
                }
            }
        }
    }
}