package st.seno.autotrading.extensions

import kotlin.math.floor
import kotlin.math.pow

fun Double.truncateToXDecimalPlaces(x: Double): Double {
    val factor = 10.0.pow(x) // 10^2 = 100
    return floor(this * factor) / factor
}