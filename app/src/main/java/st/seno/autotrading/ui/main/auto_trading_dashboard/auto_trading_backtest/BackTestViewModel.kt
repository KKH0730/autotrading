package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_backtest

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.isSuccess
import st.seno.autotrading.data.network.model.successData
import st.seno.autotrading.domain.CandleUseCase
import st.seno.autotrading.extensions.formatedDate
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.model.BackTestResult
import st.seno.autotrading.ui.base.BaseViewModel
import st.seno.autotrading.util.BookmarkUtil
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class BackTestViewModel @Inject constructor(
    private val candleUseCase: CandleUseCase,
) : BaseViewModel() {
    private val _backTestResult: MutableStateFlow<BackTestResult?> = MutableStateFlow(null)
    val backTestResult: StateFlow<BackTestResult?> get() = _backTestResult.asStateFlow()

    private val _bookmarkedTickers: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val bookmarkedTickers: StateFlow<List<String>> get() = _bookmarkedTickers.asStateFlow()

    init {
        _bookmarkedTickers.value = BookmarkUtil.convertSetToTickerList(bookmarkedTickerCodeSet = BookmarkUtil.bookmarkedTickers.value)
            .filter { it.code.split("-").size == 2 }
            .map { it.code }
    }

    fun reqBackTestData(
        marketId: String,
        initialInvestment: Double,
        sampleCount: Int,
        stopLoss: Int,
        takeProfit: Int,
        correctionValue: Double
    ) {
        vmScopeJob {
            showLoading()

            val response = candleUseCase.reqDaysCandle(
                market = marketId,
                to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
                count = sampleCount
            )
            if (response.isSuccess()) {
                val fee = 0.0005 // 거래 수수료
                var totalFee = 0.0
                var totalProfit = initialInvestment
                var tradeCount = 0
                var winCount = 0
                var maxProfitPercent = 0.0
                var maxLossPercent = 0.0
                val candles = response.successData().reversed()

                run candleLoop@{
                    candles.forEachIndexed { index, candle ->
                        if (index == 0) return@forEachIndexed

                        if (index == candles.lastIndex || totalProfit <= 5000.0) {
                            return@candleLoop
                        }

                        val breakoutPrice = candle.openingPrice + ((candles[index - 1].highPrice - candles[index - 1].lowPrice) * correctionValue) // 매수 금액
                        val stopLossPrice = breakoutPrice * (1 - (stopLoss / 100.0))
                        val takeProfitPrice = breakoutPrice * (1 + (takeProfit / 100.0))

                        if (breakoutPrice <= candle.highPrice) {
                            val bidFee = totalProfit * fee
                            val askPrice = when {
                                stopLoss > 0 -> if (stopLossPrice >= candle.lowPrice) stopLossPrice else candle.tradePrice
                                takeProfit > 0 -> if (takeProfitPrice <= candle.highPrice) takeProfitPrice else candle.tradePrice
                                else -> candle.tradePrice
                            } // 매도 금액

                            totalProfit = (((totalProfit * (1 - fee)) / breakoutPrice) * askPrice) * (1 - fee)
                            val askFee = totalProfit * fee
                            totalFee += (bidFee + askFee)

                            val changePercent = (((askPrice - breakoutPrice) / breakoutPrice) * 100)
                            if (breakoutPrice < askPrice) {
                                maxProfitPercent = max(maxProfitPercent, changePercent)
                                winCount++
                            } else if (breakoutPrice > askPrice) {
                                maxLossPercent = min(maxLossPercent, changePercent)
                            }
                            tradeCount++
                        }
                    }
                }

                _backTestResult.value = BackTestResult(
                    totalProfit = totalProfit,
                    tradeCount = tradeCount,
                    totalFee = totalFee,
                    totalReturnRate = (((totalProfit - initialInvestment) / initialInvestment) * 100.0).truncateToXDecimalPlaces(x = 2.0),
                    winRate = ((winCount / (sampleCount - 2.0)) * 100).truncateToXDecimalPlaces(x = 2.0),
                    maximumProfitRate = maxProfitPercent.truncateToXDecimalPlaces(x = 2.0),
                    maximumLossRate = maxLossPercent.truncateToXDecimalPlaces(x = 2.0),
                    trigger =_backTestResult.value?.let { result -> !result.trigger } ?: false
                )
            } else {
                showSnackbar(message = getString(R.string.network_error))
            }
            hideLoading()
        }
    }
}