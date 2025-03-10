package st.seno.autotrading.ui.main

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import st.seno.autotrading.R
import st.seno.autotrading.extensions.checkNetworkConnectivityForComposable
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.extensions.restartApp
import st.seno.autotrading.ui.common.RestartDialog
import st.seno.autotrading.ui.main.component.BottomNavigationBar
import st.seno.autotrading.ui.navigation.NavigationGraph

data class BottomTab(@StringRes val name: Int, @DrawableRes val imageRes: Int)

val bottomTabs = listOf(
    BottomTab(name = R.string.bottom_navigator_dashboard, imageRes = R.drawable.ic_dashboard),
    BottomTab(name = R.string.bottom_navigator_market, imageRes = R.drawable.ic_market),
    BottomTab(name = R.string.bottom_navigator_auto_trading, imageRes = R.drawable.ic_auto_trading),
    BottomTab(name = R.string.bottom_navigator_setting, imageRes = R.drawable.ic_setting)
)

@SuppressLint("ResourceType")
@Composable
fun MainScreen(
    tabIndex: Int,
    onChangedTabIndex: (Int) -> Unit
) {
    val context = LocalContext.current
    if (context.checkNetworkConnectivityForComposable()) {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    tabs = bottomTabs,
                    selectedTab = tabIndex,
                    onTabSelected = onChangedTabIndex::invoke
                )
            },
        ) { innerPadding ->
            Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
                NavigationGraph(navController = navController)

                navController.navigate(getString(bottomTabs[tabIndex].name)) {
                    navController.graph.startDestinationRoute?.let {
                        popUpTo(it) { saveState = true } // startDestinationRoute만 스택에 쌓일 수 있게
                    }
                    launchSingleTop = true // 화면 인스턴스 하나만 만들어지게
                    restoreState = true // 버튼을 재클릭했을 때 이전 상태가 남아있게
                }
            }
        }
    } else {
        RestartDialog(
            title = context.getString(R.string.network_error_title),
            content = context.getString(R.string.network_error),
            confirmText = context.getString(R.string.alert_dialog_restart),
            onClickConfirm = { (context as MainActivity).restartApp() }
        )
    }
}