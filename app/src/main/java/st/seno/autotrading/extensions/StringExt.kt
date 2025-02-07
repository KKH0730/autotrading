package st.seno.autotrading.extensions

import android.text.Html
import android.text.Spanned
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import st.seno.autotrading.App
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNotNullAndNotEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullAndNotEmpty != null)
    }

    return this != null && isNotEmpty()
}

fun String.fromHtml(): Spanned {
    return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
}

fun String.getDrawableResourceId(): Int {
    val context = App.getInstance()
    val resName = "@drawable/$this"
    val packName = context.packageName
    return context.resources.getIdentifier(resName, "drawable", packName)
}

@Composable
fun Int.toStr() = stringResource(id = this)

fun Double.formatPrice(): String {
    return if (this <= 0.0) {
        "0"
    } else if (this < 1){
        BigDecimal.valueOf(this).toPlainString()
    } else {
        val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
        numberFormat.format(this.truncateToXDecimalPlaces(x = 2.0))
    }
}

fun String.numberWithCommas(): String {
    return if (this.isEmpty()) {
        this
    } else {
        val decimalFormat = DecimalFormat("#,###")
        decimalFormat.format(this.toInt())
    }
}

fun String.doubleWithCommas(): String {
    return if (this.isEmpty()) {
        this
    } else {
        val decimalFormat = DecimalFormat("#,###")
        decimalFormat.format(this.toDouble())
    }
}


fun Float.numberWithCommas(): String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(this)
}

fun String.filterDigit(): String = this.filter { it.isDigit() }