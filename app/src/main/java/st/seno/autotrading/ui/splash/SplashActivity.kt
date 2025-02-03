package st.seno.autotrading.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import st.seno.autotrading.extensions.startActivity
import hideNavigationBar
import st.seno.autotrading.ui.main.MainActivity
import st.seno.autotrading.R
import st.seno.autotrading.theme.AutotradingTheme

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.hideNavigationBar()

        setContent {
            AutotradingTheme {
                Surface(Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = colorResource(id = R.color.purple_200))
                    ) {
                        SplashScreen(
                            startMain = viewModel.startMain.collectAsStateWithLifecycle(
                                initialValue = true,
                                lifecycleOwner = this@SplashActivity,
                                minActiveState = Lifecycle.State.STARTED
                            ).value
                        ) {
                            MainActivity.start(context = this@SplashActivity)
                            finish()
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(SplashActivity::class.java)
        }
    }
}