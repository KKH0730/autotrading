package st.seno.autotrading.ui.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import st.seno.autotrading.extensions.screenWidth

@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier,
    shimmerWidth: Dp = screenWidth.dp,
    shimmerHeight: Dp = 200.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.7f),
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.7f)
    )

    val transition = rememberInfiniteTransition(label = "")
    val xShimmer = transition.animateFloat(
        initialValue = 0f,
        targetValue = 4000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing, delayMillis = 0),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val shimmerBrush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(xShimmer.value - 200f, -200f),
        end = Offset(xShimmer.value + 200f, 200f),
    )

    Box(
        modifier = modifier
            .width(width = shimmerWidth)
            .height(height = shimmerHeight)
            .clip(shape = RoundedCornerShape(size = 12.dp))
            .background(brush = shimmerBrush),
        content = content
    )
}