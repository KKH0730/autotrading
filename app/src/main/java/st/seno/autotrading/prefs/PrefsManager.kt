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
}