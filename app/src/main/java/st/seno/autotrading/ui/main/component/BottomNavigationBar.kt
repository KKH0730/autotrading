package st.seno.autotrading.ui.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.extensions.toStr
import st.seno.autotrading.theme.FF2563EB
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.theme.Typography
import st.seno.autotrading.ui.main.BottomTab

@Composable
fun BottomNavigationBar(
    tabs: List<BottomTab>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = FFFFFFFF,
        modifier = Modifier.height(height = 55.dp)
    ) {
        tabs.forEachIndexed { index, tab ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onTabSelected(index) }
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = tab.imageRes),
                        contentDescription = tab.name.toStr(),
                        tint = if (selectedTab == index) FF2563EB else FF4B5563,
                        modifier = Modifier.size(24.dp)
                    )
                    5.HeightSpacer()
                    Text(
                        text = tab.name.toStr(),
                        style = Typography.labelSmall,
                        fontSize = 10.textDp,
                        color = if (selectedTab == index) FF2563EB else FF4B5563,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
    }
}