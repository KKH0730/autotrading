package st.seno.autotrading.ui.main.home.market_overview.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF2563EB
import st.seno.autotrading.theme.FF6B7280
import st.seno.autotrading.theme.FFF8F8F8
import st.seno.autotrading.theme.FFFFFFFF

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketOverviewTabRow(
    tabs: List<String>,
    tabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    SecondaryTabRow(
        selectedTabIndex = tabIndex,
        containerColor = FFFFFFFF,
        contentColor = FFF8F8F8,
        indicator = {
            SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabIndex, matchContentSize = false),
                color = FF2563EB
            )
        }
    ) {
        tabs.forEachIndexed { index, value ->
            MarketOverviewTab(
                tabName = value,
                tabIndex = tabIndex,
                position = index,
                onTabSelected = onTabSelected
            )
        }
    }
}

@Composable
fun MarketOverviewTab(
    tabName: String,
    tabIndex: Int,
    position: Int,
    onTabSelected: (Int) -> Unit
) {
    Tab(
        selected = tabIndex == position,
        selectedContentColor = FF2563EB,
        unselectedContentColor = FF6B7280,
        onClick = { onTabSelected.invoke(position) }
    ) {
        Text(
            text = tabName,
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}