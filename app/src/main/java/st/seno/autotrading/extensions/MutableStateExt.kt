package st.seno.autotrading.extensions

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState

fun <T> MutableState<T>.update(value: T) {
    this.value = value
}

fun MutableIntState.update(value: Int) {
    this.intValue = value
}