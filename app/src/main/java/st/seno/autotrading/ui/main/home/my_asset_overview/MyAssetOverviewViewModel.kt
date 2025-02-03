package st.seno.autotrading.ui.main.home.my_asset_overview

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.data.network.model.isSuccess
import st.seno.autotrading.data.network.model.successData
import st.seno.autotrading.domain.MyAssetsUseCase
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.model.TotalAsset
import st.seno.autotrading.ui.base.BaseViewModel
import st.seno.autotrading.ui.main.MainViewModel
import javax.inject.Inject

@HiltViewModel
class MyAssetOverviewViewModel @Inject constructor(
    private val myAssetsUseCase: MyAssetsUseCase
) : BaseViewModel() {
    private val myAssets: MutableStateFlow<List<Asset>> = MutableStateFlow(listOf())

    private val _totalAsset: MutableStateFlow<TotalAsset> = MutableStateFlow(TotalAsset())
    val totalAsset: StateFlow<TotalAsset> get() = _totalAsset.asStateFlow()

    init {vmScopeJob {
            reqMyAssets()
            combine(myAssets, MainViewModel.tickersMap) { a, b ->
                a to b
            }
                .collectLatest {
                    if (it.first.isEmpty() || it.second.isEmpty()) {
                        return@collectLatest
                    }

                    val assets = myAssets.value.map { asset ->
                        it.second["${getString(R.string.KRW)}-${asset.currency}"]?.let { ticker ->
                            val avgEvaluatedPrice =
                                asset.avgBuyPrice.toDouble() * asset.balance.toDouble()

                            asset.tradePrice = ticker.tradePrice
                            asset.evaluatedPrice =
                                ticker.tradePrice * asset.balance.toDouble()
                            asset.signedChangeRate = when {
                                asset.evaluatedPrice.truncateToXDecimalPlaces(x = 2.0) == avgEvaluatedPrice.truncateToXDecimalPlaces(
                                    x = 2.0
                                ) -> 0.0

                                else -> (asset.evaluatedPrice - avgEvaluatedPrice) / avgEvaluatedPrice
                            }
                        }
                        asset
                    }

                    val totalEvaluatedPrice = assets.fold(0.0) { total, asset ->
                        total + asset.evaluatedPrice
                    }

                    val currentEvaluatedPrice = assets.fold(0.0) { total, asset ->
                        total + (asset.tradePrice * asset.balance.toDouble())
                    }
                    val prevEvaluatedPrice = assets.fold(0.0) { total, asset ->
                        total + (asset.avgBuyPrice.toDouble() * asset.balance.toDouble())
                    }

                    _totalAsset.value = TotalAsset(
                        totalEvaluatedPrice = totalEvaluatedPrice,
                        totalSignedChangeRate = (currentEvaluatedPrice - prevEvaluatedPrice) / prevEvaluatedPrice,
                        assets = assets
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