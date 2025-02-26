package st.seno.autotrading.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun VerticalDashedLine(
    strokeWidth: Float = 2f,
    dashHeight: Float = 10f,
    gapHeight: Float = 10f,
    color: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            moveTo(size.width / 2, 0f)
            lineTo(size.width / 2, size.height)
        }

        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashHeight, gapHeight), 0f)

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeWidth, pathEffect = pathEffect)
        )
    }
}