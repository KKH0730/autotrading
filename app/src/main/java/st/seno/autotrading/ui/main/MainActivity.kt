package st.seno.autotrading.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import st.seno.autotrading.extensions.startActivity
import st.seno.autotrading.theme.AutotradingTheme
import st.seno.autotrading.ui.main.home.HomeViewModel
import st.seno.autotrading.ui.splash.SplashActivity


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        if (!intent.getBooleanExtra("isSplashFinish", false)) {
            SplashActivity.start(context = this@MainActivity, intent.getIntExtra("tabIndex", 0))
            finish()
        } else {
            mainViewModel.connectSocket()

            setContent {
                AutotradingTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        MainScreen(
                            tabIndex = mainViewModel.tabIndex.collectAsStateWithLifecycle(initialValue = 0, lifecycleOwner = this@MainActivity, minActiveState = Lifecycle.State.STARTED).value,
                            onChangedTabIndex = { mainViewModel.updateTabIndex(index = it) },
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mainViewModel.updateTabIndex(index = intent.getIntExtra("tabIndex", 0))
    }

    companion object {
        fun start(context: Context, tabIndex: Int) {
            context.startActivity(MainActivity::class.java) {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("isSplashFinish", true)
                putExtra("tabIndex", tabIndex)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutotradingTheme {

    }
}