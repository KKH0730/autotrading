package st.seno.autotrading.ui.main.home.market_overview

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.extensions.getBookmarkInfo
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.model.MarketTickersOverview
import st.seno.autotrading.ui.base.BaseViewModel
import st.seno.autotrading.ui.main.MainViewModel
import javax.inject.Inject

@HiltViewModel
class MarketOverviewViewModel @Inject constructor() : BaseViewModel() {
    private val _marketTickerOverview: MutableStateFlow<MarketTickersOverview> = MutableStateFlow(MarketTickersOverview())
    val marketTickerOverview: StateFlow<MarketTickersOverview> get() = _marketTickerOverview.asStateFlow()

    init {
        vmScopeJob {
            MainViewModel.tickersMap.collectLatest { tickersMap ->
                val favorites = gson.getBookmarkInfo().entries
                    .filter { it.value }
                    .mapNotNull { (key, _) -> tickersMap[key] }
                val topGainers: List<Ticker> = tickersMap.values
                    .sortedByDescending { it.signedChangeRate }
                    .toList()
                val topLosers: List<Ticker> = tickersMap.values
                    .sortedBy { it.signedChangeRate }
                    .toList()

                if (topGainers.isEmpty() || topLosers.isEmpty()) {
                    return@collectLatest
                }

                _marketTickerOverview.value = MarketTickersOverview(
                    favorites = favorites,
                    topGainers = topGainers,
                    topLosers = topLosers
                )
            }
        }
    }
}