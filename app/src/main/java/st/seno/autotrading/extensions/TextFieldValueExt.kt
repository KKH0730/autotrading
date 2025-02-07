package st.seno.autotrading.extensions

import androidx.compose.ui.text.input.TextFieldValue

fun TextFieldValue.isEmpty() = this.text.isEmpty()

fun TextFieldValue.isNotEmpty() = this.text.isNotNullAndNotEmpty()