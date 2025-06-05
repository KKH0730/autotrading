package st.seno.autotrading

import android.app.Application
import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.HiltAndroidApp
import st.seno.autotrading.prefs.PrefsManager
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

        initTimber()
        initPrefs()
        initBookmark()
        release()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initPrefs() {
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }

    private fun initBookmark() {
        BookmarkUtil.init()
    }

    private fun release() {
        PrefsManager.AutoTrading.apply {
            marketId = ""
            quantityRatio = 0
            tradingStrategy = ""
            stopLoss = 0
            stopLossPrice = ""
            takeProfit = 0
            takeProfitPrice = ""
            correctionValue = 0f
            startDate = 0L
            endDate = 0L
            tradingMode = ""
            isRunningTradingService = false
        }
        PrefsManager.Data.apply {
            isSkipBid = false
            tradePrice = 0.0
            bidOrder = null
            askOrder = null
        }
    }
}