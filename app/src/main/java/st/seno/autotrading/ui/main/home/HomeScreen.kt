package st.seno.autotrading.ui.main.home

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import st.seno.autotrading.extensions.changeBookmarkStatus
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.model.HomeContentsType
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.ui.main.MainActivity
import st.seno.autotrading.ui.main.home.component.HomeCrytoesInfo
import st.seno.autotrading.ui.main.home.component.HomeModuleShimmer
import st.seno.autotrading.ui.main.home.component.MyPortfolio
import st.seno.autotrading.ui.main.trading_view.TradingViewActivity
import st.seno.autotrading.util.BookmarkUtil

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val homeViewModel = hiltViewModel<HomeViewModel>(viewModelStoreOwner = LocalContext.current as MainActivity)
    val homeData = homeViewModel.homeContents.collectAsStateWithLifecycle(
        initialValue = listOf(),
        lifecycleOwner = LocalContext.current as MainActivity,
        minActiveState = Lifecycle.State.STARTED,
    ).value

    BackHandler { }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = FFF9FAFB)
    ) {
        if (homeData.isEmpty()) {
            HomeModuleShimmer()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(space = 24.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(homeData.size) { index ->
                    when(val data = homeData[index]) {
                        is HomeContentsType.Portfolio -> MyPortfolio(data = data)
                        is HomeContentsType.Favorites -> HomeCrytoesInfo(
                            data = data,
                            onClickCryptoItem = { TradingViewActivity.start(context = context as MainActivity, tickerCode = it) },
                            onClickBookmark = {
                                gson.changeBookmarkStatus(tickerCode = it)
                                BookmarkUtil.editBookmarkedTickers(tickerCode = it)
                            }
                        )
                        is HomeContentsType.TopGainers -> HomeCrytoesInfo(
                            data = data,
                            onClickCryptoItem = { TradingViewActivity.start(context = context as MainActivity, tickerCode = it) }
                        )
                        is HomeContentsType.TopLosers -> HomeCrytoesInfo(
                            data = data,
                            onClickCryptoItem = { TradingViewActivity.start(context = context as MainActivity, tickerCode = it) }
                        )
                    }
                }
            }
        }
    }
}

