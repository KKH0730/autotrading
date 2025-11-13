package st.seno.autotrading.server.core.extension

import com.google.gson.Gson

val gson = Gson()

inline fun <reified T> Gson.mapToModel(map: Map<String, Any?>): T {
    val json = toJson(map) // Map을 JSON 문자열로 변환
    return fromJson(json, T::class.java) // JSON을 Order 객체로 변환
}

inline fun <reified T> String.parseOrNull(): T? = try {
    gson.fromJson<T>(this, T::class.java)
} catch (e: Exception) {
    e.printStackTrace()
    null
}
