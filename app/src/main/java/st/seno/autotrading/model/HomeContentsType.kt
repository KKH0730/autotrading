package st.seno.autotrading.model

import st.seno.autotrading.data.network.model.Ticker

sealed class HomeContentsType {
    data class Portfolio(val totalAsset: TotalAsset = TotalAsset()): HomeContentsType()
    data class Favorites(val tickers: List<Ticker> = listOf()): HomeContentsType()
    data class TopLosers(val tickers: List<Ticker> = listOf()): HomeContentsType()
    data class TopGainers(val tickers: List<Ticker> = listOf()): HomeContentsType()
}
