package st.seno.autotrading.ui.main.market

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import st.seno.autotrading.extensions.getBookmarkInfo
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.model.MarketTickers
import st.seno.autotrading.ui.base.BaseViewModel
import st.seno.autotrading.ui.main.MainViewModel
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

            ) { a, b, c -> Triple(a, b, c) }
                .collectLatest {
                    val tickersMap = MainViewModel.tickersMap.value

                    val krwTickers = it.first.values
                        .sortedBy { ticker -> ticker.code }
                        .toList()
                    val btcTickers = it.second.values
                        .sortedBy { ticker -> ticker.code }
                        .toList()
                    val usdtTickers = it.third.values
                        .sortedBy { ticker -> ticker.code }
                        .toList()
                    val favoriteTickers = gson.getBookmarkInfo().entries
                        .filter { entries -> entries.value }
                        .mapNotNull { (key, _) -> tickersMap[key] }

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