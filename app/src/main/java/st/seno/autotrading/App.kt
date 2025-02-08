package st.seno.autotrading

import android.app.Application
import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.HiltAndroidApp
import st.seno.autotrading.util.BookmarkUtil
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    
    companion object {
        private lateinit var instance: App
        fun getInstance(): App = instance
    }


    override fun onCreate() {
        super.onCreate()
        instance = this


        Timber.plant(Timber.DebugTree())

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        BookmarkUtil.init()
    }
}