package st.seno.autotrading.ui.main.auto_trading_dashboard

import android.annotation.SuppressLint
import android.icu.util.Calendar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import st.seno.autotrading.data.network.model.ClosedOrder
import st.seno.autotrading.data.network.model.isSuccess
import st.seno.autotrading.data.network.model.successData
import st.seno.autotrading.domain.OrderUseCase
import st.seno.autotrading.domain.TradingDataUseCase
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.model.Side
import st.seno.autotrading.ui.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AutoTradingDashboardViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase,
    private val tradingDataUseCase: TradingDataUseCase
) : BaseViewModel() {

    private val _selectedCryptoToTradingHistory: MutableStateFlow<String> =
        MutableStateFlow("KRW-BTC")
    val selectedCryptoToTradingHistory: StateFlow<String> get() = _selectedCryptoToTradingHistory.asStateFlow()

    @SuppressLint("SimpleDateFormat")
    private val _selectedDateToTradingHistory: MutableStateFlow<Long> =
        MutableStateFlow(Calendar.getInstance().timeInMillis)
    val selectedDateToTradingHistory: StateFlow<Long> get() = _selectedDateToTradingHistory.asStateFlow()

    private val _tradingHistories: MutableStateFlow<List<ClosedOrder>?> = MutableStateFlow(null)
    val tradingHistories: StateFlow<List<ClosedOrder>?> get() = _tradingHistories.asStateFlow()

    private val _signedChangeRate: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val signedChangeRate: StateFlow<Double> get() = _signedChangeRate.asStateFlow()


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
            val result = orderUseCase.reqClosedOrders(
                marketId = _selectedCryptoToTradingHistory.value,
                states = arrayOf("done, cancel"),
                startTime = null,
                endTime = calendar.timeInMillis.toString(),
                limit = 100
            )

            if (result.isSuccess()) {
                _tradingHistories.value = result.successData()
            }
        }
    }

    fun reqTradingData(startDate: String) {
        vmScopeJob {
            tradingDataUseCase.reqTradingData(startDate = startDate).collectLatest {
                _signedChangeRate.value = if (it.isEmpty()) {
                    0.0
                } else {
                    val isAlreadyAsk = it.firstOrNull { tradingData ->  tradingData.order.side == Side.ASK.value } != null
                    if (isAlreadyAsk) {
                        it.fold(0.0 to 0.0) { acc, tradingData ->
                            var totalBidPrice = acc.first
                            var totalAskPrice = acc.second
                            if (tradingData.order.side == Side.BID.value) {
                                totalBidPrice += tradingData.order.trades?.sumOf { trade -> trade.tradesFunds.toDouble() + tradingData.order.paidFee.toDouble() } ?: 0.0
                            } else {
                                totalAskPrice += tradingData.order.trades?.sumOf { trade -> trade.tradesFunds.toDouble() - tradingData.order.paidFee.toDouble() } ?: 0.0
                            }

                            totalBidPrice to totalAskPrice
                        }
                            .let { pair -> ((pair.second - pair.first) / pair.first) * 100 }
                            .truncateToXDecimalPlaces(x = 2.0)
                    } else {
                        0.0
                    }
                }
            }
        }
    }
}