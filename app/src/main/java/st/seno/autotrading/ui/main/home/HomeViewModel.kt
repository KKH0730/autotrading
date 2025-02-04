package st.seno.autotrading.ui.main.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.data.network.model.isSuccess
import st.seno.autotrading.data.network.model.successData
import st.seno.autotrading.domain.MyAssetsUseCase
import st.seno.autotrading.extensions.getBookmarkInfo
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.model.HomeContentsType
import st.seno.autotrading.model.TotalAsset
import st.seno.autotrading.ui.base.BaseViewModel
import st.seno.autotrading.ui.main.MainViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val myAssetsUseCase: MyAssetsUseCase
) : BaseViewModel() {
    private val myAssets: MutableStateFlow<List<Asset>> = MutableStateFlow(listOf())

    private val _homeContents: MutableStateFlow<List<HomeContentsType>> = MutableStateFlow(listOf())
    val homeContents: StateFlow<List<HomeContentsType>> get() = _homeContents.asStateFlow()

    init {
        vmScopeJob {
            reqMyAssets()
            combine(myAssets, MainViewModel.tickersMap) { a, b -> a to b }
                .collectLatest {
                    val myAssets = it.first
                    val favorites = gson.getBookmarkInfo().entries
                        .filter { it.value }
                        .mapNotNull { (key, _) -> it.second[key] }
                    val topGainers: List<Ticker> = it.second.values
                        .sortedByDescending { it.signedChangeRate }
                        .toList()
                    val topLosers: List<Ticker> = it.second.values
                        .sortedBy { it.signedChangeRate }
                        .toList()

                    if (myAssets.isEmpty() || topGainers.isEmpty() || topLosers.isEmpty()) {
                        return@collectLatest
                    }

                    val assets = myAssets.map { asset ->
                        val tickersMap = MainViewModel.tickersMap.value

                        tickersMap["${getString(R.string.KRW)}-${asset.currency}"]?.let { ticker ->
                            val avgEvaluatedPrice = asset.avgBuyPrice.toDouble() * asset.balance.toDouble()

                            asset.evaluatedPrice = ticker.tradePrice * asset.balance.toDouble()
                            asset.signedChangeRate = when {
                                asset.evaluatedPrice.truncateToXDecimalPlaces(x = 2.0) == avgEvaluatedPrice.truncateToXDecimalPlaces(x = 2.0) -> 0.0
                                else -> (asset.evaluatedPrice - avgEvaluatedPrice) / avgEvaluatedPrice
                            }
                        }

                        if (asset.currency.lowercase() == getString(R.string.krw)) {
                            asset.balance = asset.balance.toDouble().truncateToXDecimalPlaces(x = 2.0).toString()
                        }

                        asset
                    }
                    val myTotalEvaluatedPrice = assets.fold(0.0) { total, asset ->
                        if (asset.currency.lowercase() == getString(R.string.krw)) {
                            total + asset.balance.toDouble()
                        } else {
                            total + asset.evaluatedPrice
                        }
                    }

                    _homeContents.value = listOf(
                        HomeContentsType.Portfolio(totalAsset = TotalAsset(totalEvaluatedPrice = myTotalEvaluatedPrice, assets = assets)),
                        HomeContentsType.Favorites(tickers = favorites),
                        HomeContentsType.TopGainers(tickers = topGainers),
                        HomeContentsType.TopLosers(tickers = topLosers)
                    )
                }

        }
    }

    private suspend fun reqMyAssets() {
        myAssetsUseCase.reqMyAssets().collectLatest {
            if (it.isSuccess()) {
                myAssets.value = it.successData()
            }
        }
    }
}