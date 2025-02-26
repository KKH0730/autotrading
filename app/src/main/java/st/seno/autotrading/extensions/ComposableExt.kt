package st.seno.autotrading.extensions

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import st.seno.autotrading.theme.DIVIDER
import kotlin.math.min

@Composable
fun Int.HeightSpacer(modifier: Modifier = Modifier) = Spacer(modifier = modifier.height(height = this.dp))

@Composable
fun Int.WidthSpacer(modifier: Modifier = Modifier) = Spacer(modifier = modifier.width(width = this.dp))

@Composable
fun RowScope.FullWidthSpacer(modifier: Modifier = Modifier) = Spacer(modifier = modifier.weight(weight = 1f))

@Composable
fun ColumnScope.FullHeightSpacer(modifier: Modifier = Modifier) = Spacer(modifier = modifier.weight(weight = 1f))

@Composable
fun Int.Divider(
    xOffset: Double = 0.0,
    yOffset: Double = 0.0,
    backgroundColor: Color = DIVIDER,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .height(height = this.dp)
            .fillMaxWidth()
            .offset(x = xOffset.dp, y = yOffset.dp)
            .background(color = backgroundColor)
    )
}

@Composable
fun Double.Divider(
    xOffset: Double = 0.0,
    yOffset: Double = 0.0,
    backgroundColor: Color = DIVIDER,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .height(height = this.dp)
            .fillMaxWidth()
            .offset(x = xOffset.dp, y = yOffset.dp)
            .background(color = backgroundColor)
    )
}

@Composable
fun Int.VerticalDivider(
    xOffset: Double = 0.0,
    yOffset: Double = 0.0,
    backgroundColor: Color = DIVIDER,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .fillMaxHeight()
            .width(width = this.dp)
            .offset(x = xOffset.dp, y = yOffset.dp)
            .background(color = backgroundColor)
    )
}

@Composable
fun Double.VerticalDivider(
    xOffset: Double = 0.0,
    yOffset: Double = 0.0,
    backgroundColor: Color = DIVIDER,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .fillMaxHeight()
            .width(width = this.dp)
            .offset(x = xOffset.dp, y = yOffset.dp)
            .background(color = backgroundColor)
    )
}
