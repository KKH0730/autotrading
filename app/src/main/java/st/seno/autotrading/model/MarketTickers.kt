package st.seno.autotrading.model

import st.seno.autotrading.data.network.model.Ticker

data class MarketTickers(
    val krwTickers: List<Ticker> = listOf(),
    val btcTickers: List<Ticker> = listOf(),
    val usdtTickers: List<Ticker> = listOf(),
    val favoriteTickers: List<Ticker> = listOf(),
)