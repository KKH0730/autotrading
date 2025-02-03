package st.seno.autotrading.ui.main.home.my_asset_overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import st.seno.autotrading.R
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.ui.common.CommonToolbar
import st.seno.autotrading.ui.main.home.my_asset_overview.component.AssetOverviewPanel
import st.seno.autotrading.ui.main.home.my_asset_overview.component.CryptoOverviewPanel
import st.seno.autotrading.ui.main.home.my_asset_overview.component.MyAssetOverviewShimmer

@Composable
fun MyAssetScreen(onClickBack: () -> Unit) {
    val myAssetOverviewViewModel = hiltViewModel<MyAssetOverviewViewModel>()
    val totalAsset = myAssetOverviewViewModel.totalAsset.collectAsStateWithLifecycle().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = FFF9FAFB)
    ) {
        if (totalAsset.assets.isEmpty()) {
            MyAssetOverviewShimmer(onClickBack = onClickBack)
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                CommonToolbar(
                    title = stringResource(R.string.assets_overview_title),
                    onClickBack = onClickBack
                )
                16.HeightSpacer()
                AssetOverviewPanel(
                    data = totalAsset,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
                16.HeightSpacer()
                CryptoOverviewPanel(
                    assets = totalAsset.assets,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
        }
    }
}