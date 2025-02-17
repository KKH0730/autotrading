package st.seno.autotrading.extensions

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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
    inputFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"),
    outputFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
): String {
    val parsedDate = LocalDateTime.parse(this, inputFormatter)
    return parsedDate.format(outputFormatter)
}

fun String.isFirstDayOfMonth(): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.parse(this, formatter)
    return dateTime.dayOfMonth == 1
}

fun String.isToday(): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDate.parse(this, formatter)
    return dateTime == LocalDate.now()
}

fun Calendar.calendarToLocalDateTime(zoneId: ZoneId): LocalDateTime {
    val instant = this.toInstant()
    return LocalDateTime.ofInstant(instant, zoneId)
}

fun String.formatedDate(): String {
    val localNow = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
    val utcNow = localNow.withZoneSameInstant(ZoneOffset.UTC)
    val formatter = DateTimeFormatter.ofPattern(this)
    return utcNow.format(formatter)
}

fun String.utcToKoreanTime(): String {
    val utcTime = ZonedDateTime.parse(this + "Z", DateTimeFormatter.ISO_DATE_TIME)
    val seoulTime = utcTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"))
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return seoulTime.format(formatter)
}

fun String.kstToKoreanTime(): String {
    // KST 시간 문자열을 LocalDateTime으로 파싱
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val localDateTime = LocalDateTime.parse(this, formatter)
    val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Asia/Seoul"))
    return zonedDateTime.format(formatter)
}