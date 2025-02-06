package st.seno.autotrading.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gun0912.tedpermission.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import hideNavigationBar
import st.seno.autotrading.R
import st.seno.autotrading.extensions.startActivity
import st.seno.autotrading.prefs.PrefsManager
import st.seno.autotrading.theme.AutotradingTheme
import st.seno.autotrading.ui.main.MainActivity
import st.seno.autotrading.util.PermissionUtil

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
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                startPermission(onComplete = {
                                    MainActivity.start(context = this@SplashActivity, tabIndex = intent.getIntExtra("tabIndex", 0))
                                    finish()
                                })
                            } else {
                                MainActivity.start(context = this@SplashActivity, tabIndex = intent.getIntExtra("tabIndex", 0))
                                finish()
                            }

                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun startPermission(onComplete: () -> Unit) {
        PermissionUtil.requestNotificationPermission(object : PermissionListener {
            override fun onPermissionGranted() {
                onComplete.invoke()
                PrefsManager.isCompleteRequestNotification = true
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                onComplete.invoke()
                PrefsManager.isCompleteRequestNotification = false
            }
        })
    }

    companion object {
        fun start(context: Context, tabIndex: Int) {
            context.startActivity(SplashActivity::class.java) {
                putExtra("tabIndex", tabIndex)
            }
        }
    }
}