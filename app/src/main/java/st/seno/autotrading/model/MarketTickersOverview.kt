package st.seno.autotrading.model

import st.seno.autotrading.data.network.model.Ticker

data class MarketTickersOverview(
    val topGainers: List<Ticker> = listOf(),
    val topLosers: List<Ticker> = listOf(),
    val favorites: List<Ticker> = listOf()
)