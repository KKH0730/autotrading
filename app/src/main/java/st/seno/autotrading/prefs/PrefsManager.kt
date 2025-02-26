package st.seno.autotrading.prefs

import com.pixplicity.easyprefs.library.Prefs

object PrefsManager {
    var isCompleteRequestNotification: Boolean
        get() = Prefs.getBoolean("notification", false)
        set(isComplete) {
            Prefs.putBoolean("notification", isComplete)
        }

    var marketAll: String
        get() = Prefs.getString("marketAll", "")
        set(value) {
            Prefs.putString("marketAll", value)
        }

    var marketIdList: String
        get() = Prefs.getString("marketIdList", "")
        set(value) {
            Prefs.putString("marketIdList", value)
        }

    var bookmark: String
        get() = Prefs.getString("bookmark", "")
        set(value) {
            Prefs.putString("bookmark", value)
        }

    var riseColor: Long
        get() = Prefs.getLong("riseColor", 0xFF16A34A)
        set(value) {
            Prefs.putLong("riseColor", value)
        }

    var fallColor: Long
        get() = Prefs.getLong("fallColor", 0xFFDC2626)
        set(value) {
            Prefs.putLong("fallColor", value)
        }

    var evenColor: Long
        get() = Prefs.getLong("evenColor", 0xFF4B5563)
        set(value) {
            Prefs.putLong("evenColor", value)
        }

    var selectedColorIndex: Int
        get() = Prefs.getInt("selectedColorIndex", 0)
        set(value) {
            Prefs.putInt("selectedColorIndex", value)
        }
}