package st.seno.autotrading.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import st.seno.autotrading.theme.DIVIDER

@Composable
fun Int.HeightSpacer(modifier: Modifier = Modifier) = Spacer(modifier = modifier.height(height = this.dp))

@Composable
fun Int.WidthSpacer(modifier: Modifier = Modifier) = Spacer(modifier = modifier.width(width = this.dp))

@Composable
fun RowScope.FullWidthSpacer(modifier: Modifier = Modifier) = Spacer(modifier = modifier.weight(weight = 1f))

@Composable
fun ColumnScope.FullHeightSpacer(modifier: Modifier = Modifier) = Spacer(modifier = modifier.weight(weight = 1f))

@Composable
fun Int.Divider(modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier
            .height(height = this.dp)
            .fillMaxWidth()
            .background(color = DIVIDER)
    )
}