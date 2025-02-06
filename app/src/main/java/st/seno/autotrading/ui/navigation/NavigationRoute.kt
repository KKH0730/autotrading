package st.seno.autotrading.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import st.seno.autotrading.ui.main.auto_trading_dashboard.AutoTradingDashboardScreen
import st.seno.autotrading.ui.main.home.HomeScreen
import st.seno.autotrading.ui.main.market.MarketScreen

enum class NavigationRoute(val routeName: String) {
    DASHBOARD("Dashboard"),
    MARKET("MARKET"),
    AUTO_TRADING("Auto Trading"),
    SETTINGS("Settings")
}

@SuppressLint("RememberReturnType")
@Composable
fun NavigationGraph(startRoute: NavigationRoute = NavigationRoute.DASHBOARD, navController: NavHostController) {
    val routeAction = remember(navController) { RouteAction(navController) }

    NavHost(navController = navController, startDestination = startRoute.routeName) {
        composable(NavigationRoute.DASHBOARD.routeName) {
            HomeScreen()
        }
        composable(NavigationRoute.MARKET.routeName) {
            MarketScreen()
        }
        composable(NavigationRoute.AUTO_TRADING.routeName) {
            AutoTradingDashboardScreen()
        }
        composable(NavigationRoute.SETTINGS.routeName) {
            MarketScreen()
        }
    }
}