package st.seno.autotrading.extensions

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("SimpleDateFormat")
fun Long.toDate(pattern: String): String {
    val simpleDateFormat = SimpleDateFormat(pattern)
    val calendar = Calendar.getInstance().apply {
        timeInMillis  = this@toDate
    }
    return simpleDateFormat.format(Date().apply { time = calendar.timeInMillis })
}

fun String.parseDateFormat(
    originalFormat:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()),
    targetFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM.dd HH:mm", Locale.getDefault())
): String {
    return try {
        val date = originalFormat.parse(this)
        val formattedDate = targetFormat.format(date!!)
        formattedDate
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
