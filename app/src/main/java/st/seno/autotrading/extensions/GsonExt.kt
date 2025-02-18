package st.seno.autotrading.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import st.seno.autotrading.data.network.model.Crypto
import st.seno.autotrading.prefs.PrefsManager
import timber.log.Timber

val gson = Gson()

fun Gson.getCryptoKoName(key: String): String {
    val type = object : TypeToken<Map<String, Crypto>>() {}.type
    val cryptoMap = this.fromJson<Map<String, Crypto>>(PrefsManager.marketAll, type)
    return cryptoMap[key]?.koName ?: ""
}

fun Gson.getCryptoEnName(key: String): String {
    val type = object : TypeToken<Map<String, Crypto>>() {}.type
    val cryptoMap = this.fromJson<Map<String, Crypto>>(PrefsManager.marketAll, type)
    return cryptoMap[key]?.enName ?: ""
}

fun Gson.getBookmarkInfo(): Map<String, Boolean> {
    val type = object : TypeToken<Map<String, Boolean>>() {}.type
    val bookmarkMap = this.fromJson<Map<String, Boolean>>(PrefsManager.bookmark, type)
    return bookmarkMap ?: mapOf()
}

fun Gson.changeBookmarkStatus(tickerCode: String) {
    try {
        val bookmarkMap = this.getBookmarkInfo().toMutableMap()
        bookmarkMap[tickerCode] = bookmarkMap[tickerCode]?.let { !it } ?: false
        val json = Gson().toJson(bookmarkMap)
        PrefsManager.bookmark = json
    } catch (e: Exception) {
        e.printStackTrace()
        Timber.e(e)
    }
}

inline fun <reified T> Gson.mapToModel(map: Map<String, Any?>): T {
    val json = toJson(map) // Map을 JSON 문자열로 변환
    return fromJson(json, T::class.java) // JSON을 Order 객체로 변환
}