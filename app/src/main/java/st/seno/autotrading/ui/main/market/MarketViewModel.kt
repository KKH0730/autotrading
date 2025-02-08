package st.seno.autotrading.ui.main.market

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.model.MarketTickers
import st.seno.autotrading.ui.base.BaseViewModel
import st.seno.autotrading.ui.main.MainViewModel
import st.seno.autotrading.util.BookmarkUtil
import st.seno.autotrading.util.BookmarkUtil.bookmarkedTickers
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor() : BaseViewModel() {
    private val _marketTickers: MutableStateFlow<MarketTickers> = MutableStateFlow(MarketTickers())
    val marketTickers: StateFlow<MarketTickers> get() = _marketTickers.asStateFlow()

    init {
        vmScopeJob {
            combine(
                MainViewModel.krwTickers,
                MainViewModel.btcTickers,
                MainViewModel.usdtTickers,
                bookmarkedTickers
            ) { a, b, c, d -> MarketDataModel(krwTickers = a, btcTickers = b, usdtTickers = c, bookmarkedTickers = d) }
                .collectLatest {
                    val krwTickers = it.krwTickers.values
                        .sortedBy { ticker -> ticker.code }
                        .toList()
                    val btcTickers = it.btcTickers.values
                        .sortedBy { ticker -> ticker.code }
                        .toList()
                    val usdtTickers = it.usdtTickers.values
                        .sortedBy { ticker -> ticker.code }
                        .toList()

                    val favoriteTickers = BookmarkUtil.convertSetToTickerList(bookmarkedTickerCodeSet = it.bookmarkedTickers)

                    _marketTickers.value = MarketTickers(
                        krwTickers = krwTickers,
                        btcTickers = btcTickers,
                        usdtTickers = usdtTickers,
                        favoriteTickers = favoriteTickers
                    )
                }
        }
    }
}

data class MarketDataModel(
    val krwTickers: MutableMap<String, Ticker>,
    val btcTickers: MutableMap<String, Ticker>,
    val usdtTickers: MutableMap<String, Ticker>,
    val bookmarkedTickers: Set<String>
)