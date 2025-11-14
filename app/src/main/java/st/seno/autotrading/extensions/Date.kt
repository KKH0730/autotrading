package st.seno.autotrading.extensions

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.Instant
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
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault()) // 시스템 시간대 사용
        .toLocalDate()
        .format(formatter)
}

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.of("Asia/Seoul"))
        .toLocalDateTime()
        .plusDays(1)
        .withHour(8)
        .withMinute(59)
        .withSecond(59)
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

fun String.stringToLocalDateTime(pattern: String): LocalDateTime = LocalDateTime.parse(this, DateTimeFormatter.ofPattern(pattern))


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

fun String.toSeoulTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val localDateTime = LocalDateTime.parse(this, formatter)
    val utcZoned = localDateTime.atZone(ZoneId.of("UTC"))
    val seoulZoned = utcZoned.withZoneSameInstant(ZoneId.of("Asia/Seoul"))
    return seoulZoned.format(formatter)
}

fun String.kstToKoreanTime(): String {
    // KST 시간 문자열을 LocalDateTime으로 파싱
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val localDateTime = LocalDateTime.parse(this, formatter)
    val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Asia/Seoul"))
    return zonedDateTime.format(formatter)
}

fun Pair<Long, Long>.daysBetween(): Long {
    val startDate = Calendar.getInstance().apply {
        timeInMillis = this@daysBetween.first
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val endDate = Calendar.getInstance().apply {
        timeInMillis = this@daysBetween.second
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return (endDate.timeInMillis - startDate.timeInMillis) / (1000 * 60 * 60 * 24)
}

fun Long.formatDate(): String  {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault()) // 로컬 시간대 사용
    val instant = Instant.ofEpochMilli(this)
    return formatter.format(instant)
}