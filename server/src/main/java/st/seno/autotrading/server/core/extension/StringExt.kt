package st.seno.autotrading.server.core.extension

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.math.abs

@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNotNullAndNotEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullAndNotEmpty != null)
    }

    return this != null && isNotEmpty()
}

fun Double.formatRealPrice(): String {
    return if (abs(this) <= 0.0) {
        "0"
    } else if (abs(this) < 1){
        BigDecimal.valueOf(this).toPlainString()
    } else {
        val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
        numberFormat.format(this)
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

fun Double.numberWithCommas(): String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(this)
}


fun Int.numberWithCommas(): String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(this)
}


fun Float.numberWithCommas(): String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(this)
}

fun Double.truncateToDecimalPlaces(x: Int): String {
    return BigDecimal(this)
        .setScale(x, RoundingMode.DOWN) // 소수점 6자리까지 유지, 이하 버림
        .stripTrailingZeros() // 불필요한 0 제거
        .toPlainString() // 지수 표기 방지
}


fun String.filterDigit(): String = this.filter { it.isDigit() }