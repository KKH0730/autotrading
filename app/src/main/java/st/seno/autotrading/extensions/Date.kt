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
    return simpleDateFormat.format(Date().time.apply { calendar.timeInMillis })
}

fun String.parseDateFormat(
    originalFormat:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()),
    targetFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM.dd HH:mm", Locale.getDefault())
): String {
    return try {
        // 문자열을 Date 객체로 변환
        val date = originalFormat.parse(this)

        // 원하는 형식으로 변환
        val formattedDate = targetFormat.format(date!!)

        formattedDate
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
