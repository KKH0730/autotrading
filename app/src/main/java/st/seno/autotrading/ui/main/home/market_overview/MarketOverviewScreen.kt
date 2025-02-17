package st.seno.autotrading.ui.main.home.market_overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import st.seno.autotrading.R
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.ui.common.CommonToolbar
import st.seno.autotrading.ui.main.home.market_overview.component.FavoritesOverview
import st.seno.autotrading.ui.main.home.market_overview.component.MarketOverviewShimmer
import st.seno.autotrading.ui.main.home.market_overview.component.MarketOverviewTabRow
import st.seno.autotrading.ui.main.home.market_overview.component.TopGainersOverview
import st.seno.autotrading.ui.main.home.market_overview.component.TopLosersOverview
import st.seno.autotrading.ui.main.trading_view.TradingViewActivity
import timber.log.Timber

val marketOverviewTabs = listOf(
    getString(R.string.home_favorite),
    getString(R.string.home_top_gainers),
    getString(R.string.home_top_losers)
)

@Composable
fun MarketOverviewScreen(
    initialPosition: Int,
    onClickBack: () -> Unit
) {
    val context = LocalContext.current
    val marketOverviewViewModel = hiltViewModel<MarketOverviewViewModel>()
    val marketTickerOverview = marketOverviewViewModel.marketTickerOverview.collectAsStateWithLifecycle(
        lifecycleOwner = LocalContext.current as MarketOverviewActivity,
        minActiveState = Lifecycle.State.STARTED,
    ).value

    val coroutineScope = rememberCoroutineScope()
    val tabIndex = remember { mutableIntStateOf(initialPosition) }
    val pagerState = rememberPagerState(
        pageCount = { marketOverviewTabs.size },
        initialPageOffsetFraction = 0f,
        initialPage = initialPosition,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = FFF9FAFB)
    ) {
        if (marketTickerOverview.topGainers.isEmpty() || marketTickerOverview.topLosers.isEmpty()) {
            MarketOverviewShimmer(onClickBack = onClickBack)
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                CommonToolbar(
                    title = stringResource(R.string.market_overview_title),
                    onClickBack = onClickBack
                )
                Column {
                    MarketOverviewTabRow(
                        tabs = marketOverviewTabs,
                        tabIndex = tabIndex.intValue,
                        onTabSelected = { index ->
                            tabIndex.intValue = index

                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )

                    HorizontalPager(
                        state = pagerState,
                        userScrollEnabled = true,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        LaunchedEffect(pagerState.currentPage) {
                            tabIndex.intValue = page
                        }

                        when(page) {
                            0 -> FavoritesOverview(
                                tickers = marketTickerOverview.favorites,
                                onClickCryptoItem = { TradingViewActivity.start(context = context, tickerCode = it) }
                            )
                            1 -> TopGainersOverview(
                                tickers = marketTickerOverview.topGainers,
                                onClickCryptoItem = { TradingViewActivity.start(context = context, tickerCode = it) }
                            )
                            else -> TopLosersOverview(
                                tickers = marketTickerOverview.topLosers,
                                onClickCryptoItem = { TradingViewActivity.start(context = context, tickerCode = it) }
                            )
                        }
                    }
                }
            }
        }
    }
}