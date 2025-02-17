package st.seno.autotrading.extensions

import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.TypedValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import st.seno.autotrading.theme.FF000000

@Composable
fun String.measuredTextWidth(
    fontSize: Int,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = FF000000
): Float {
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = textMeasurer.measure(
        text = this,
        style = TextStyle(
            fontSize = fontSize.textDp,
            fontWeight = fontWeight,
            color = color
        )
    )
    return textLayoutResult.size.width.toFloat()
}
@Composable
fun String.measuredTextHeight(
    fontSize: Int,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = FF000000
): Float {
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = textMeasurer.measure(
        text = this,
        style = TextStyle(
            fontSize = fontSize.textDp,
            fontWeight = fontWeight,
            color = color
        )
    )
    return textLayoutResult.size.height.toFloat()
}

fun String.measuredTextWidth(
    fontSize: Float,
    typeface: Typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
): Float {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = fontSize // px 단위
        this.typeface = typeface
    }
    return paint.measureText(this)
}


fun String.measureTextWidth(fontSizeSp: Float, typeface: Typeface = Typeface.DEFAULT): Float {
    val paint = TextPaint().apply {
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, fontSizeSp, Resources.getSystem().displayMetrics
        )
        this.typeface = typeface
        isAntiAlias = true
    }

    val layout = StaticLayout.Builder.obtain(this, 0, this.length, paint, Int.MAX_VALUE)
        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
        .setLineSpacing(0f, 1f)
        .setIncludePad(false)
        .build()

    return layout.getLineWidth(0) // 첫 번째 줄의 너비 반환
}