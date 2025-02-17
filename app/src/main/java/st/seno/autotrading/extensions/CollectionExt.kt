package st.seno.autotrading.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun <T> Collection<T>?.isNotNullAndNotEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullAndNotEmpty != null)
    }

    return this != null && isNotEmpty()
}
