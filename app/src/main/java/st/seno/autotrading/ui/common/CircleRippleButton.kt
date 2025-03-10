package st.seno.autotrading.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp

@Composable
fun CircleRippleButton(
    size: Dp,
    onClick: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    Box(
        modifier = Modifier
            .size(size = size)
            .clip(shape = CircleShape)
            .clickable(onClick = onClick)
    ) {
        content.invoke(Modifier.align(alignment = Alignment.Center))
    }
}
@Composable
fun CircleRippleNotFormatSizedButton(
    content: @Composable (Modifier) -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(shape = CircleShape)
            .clickable(onClick = onClick)
    ) {
        content.invoke(Modifier.align(alignment = Alignment.Center))
    }
}