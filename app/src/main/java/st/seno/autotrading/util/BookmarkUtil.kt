package st.seno.autotrading.util

import kotlinx.coroutines.flow.MutableStateFlow
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.extensions.getBookmarkInfo
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.ui.main.MainViewModel

object BookmarkUtil {
    val bookmarkedTickers = MutableStateFlow<Set<String>>(setOf())

    fun init() {
        bookmarkedTickers.value = gson.getBookmarkInfo()
            .filter { it.value }
            .keys
    }

    fun editBookmarkedTickers(tickerCode: String) {
        val bookmarkedTickersSet = bookmarkedTickers.value.toMutableSet()
        if (bookmarkedTickersSet.contains(tickerCode)) {
            bookmarkedTickersSet.remove(tickerCode)
        } else {
            bookmarkedTickersSet.add(tickerCode)
        }
        bookmarkedTickers.value = bookmarkedTickersSet.toSet()
    }

     fun convertSetToTickerList(bookmarkedTickerCodeSet: Set<String>): List<Ticker> {
         val tickersMap = MainViewModel.tickersMap.value
         return bookmarkedTickerCodeSet.mapNotNull { tickerCode -> tickersMap[tickerCode] }
     }
}