package st.seno.autotrading.ui.main.market

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import st.seno.autotrading.R
import st.seno.autotrading.extensions.changeBookmarkStatus
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.model.MarketTickers
import st.seno.autotrading.prefs.PrefsManager
import st.seno.autotrading.ui.main.MainActivity
import st.seno.autotrading.ui.main.market.component.MarketFavoritesView
import st.seno.autotrading.ui.main.market.component.MarketTabRow
import st.seno.autotrading.ui.main.market.component.MarketView
import st.seno.autotrading.ui.main.trading_view.TradingViewActivity
import st.seno.autotrading.util.BookmarkUtil
import st.seno.autotrading.util.BookmarkUtil.bookmarkedTickers
import timber.log.Timber

@Composable
fun MarketScreen() {
    val context = LocalContext.current
    val marketViewModel = hiltViewModel<MarketViewModel>()
    val marketTicker = marketViewModel.marketTickers.collectAsStateWithLifecycle(
        initialValue = MarketTickers(),
        lifecycleOwner = LocalContext.current as MainActivity,
        minActiveState = Lifecycle.State.STARTED
    ).value
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf(
        stringResource(R.string.market_tab_1),
        stringResource(R.string.market_tab_2),
        stringResource(R.string.market_tab_3),
        stringResource(R.string.market_tab_4)
    )
    val tabIndex = remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(
        pageCount = { tabs.size },
        initialPageOffsetFraction = 0f,
        initialPage = 0,
    )

    BackHandler {  }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MarketTabRow(
            tabs = tabs,
            tabIndex = tabIndex.intValue,
            onTabSelected = { index ->
                tabIndex.intValue = index

                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )
        HorizontalPager(state = pagerState, userScrollEnabled = true) { page ->
            LaunchedEffect(pagerState.currentPage) {
                tabIndex.intValue = pagerState.currentPage
            }

            when(page) {
                0 -> MarketView(
                    tickers = marketTicker.krwTickers,
                    bookmarkedTickers = marketTicker.favoriteTickers,
                    onClickCryptoItem = { TradingViewActivity.start(context = context, tickerCode = it) },
                    onClickBookmark = {
                        gson.changeBookmarkStatus(tickerCode = it)
                        BookmarkUtil.editBookmarkedTickers(tickerCode = it)
                    }
                )
                1 -> MarketView(
                    tickers = marketTicker.btcTickers,
                    bookmarkedTickers = marketTicker.favoriteTickers,
                    onClickCryptoItem = { TradingViewActivity.start(context = context, tickerCode = it) },
                    onClickBookmark = {
                        gson.changeBookmarkStatus(tickerCode = it)
                        BookmarkUtil.editBookmarkedTickers(tickerCode = it)
                    }
                )
                2 -> MarketView(
                    tickers = marketTicker.usdtTickers,
                    bookmarkedTickers = marketTicker.favoriteTickers,
                    onClickCryptoItem = { TradingViewActivity.start(context = context, tickerCode = it) },
                    onClickBookmark = {
                        gson.changeBookmarkStatus(tickerCode = it)
                        BookmarkUtil.editBookmarkedTickers(tickerCode = it)
                    }
                )
                else -> MarketFavoritesView(
                    tickers = marketTicker.favoriteTickers,
                    onClickCryptoItem = { TradingViewActivity.start(context = context, tickerCode = it) },
                    onClickBookmark = {
                        gson.changeBookmarkStatus(tickerCode = it)
                        BookmarkUtil.editBookmarkedTickers(tickerCode = it)
                    }
                )
            }
        }
    }
}