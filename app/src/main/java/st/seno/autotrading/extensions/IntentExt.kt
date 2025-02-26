package st.seno.autotrading.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat

fun Context.startActivity(action: String, builder: (Intent.() -> Unit)) {
    startActivity(Intent(action).apply(builder))
}

fun <T> Context.startActivity(
    activityClass: Class<T>,
    builder: (Intent.() -> Unit)
) {
    startActivity(Intent(this, activityClass).apply(builder))
}

fun <T> Context.startActivity(
    activityClass: Class<T>
) {
    startActivity(Intent(this, activityClass))
}

fun <T> Context.startActivity(
    activityClass: Class<T>,
    launcher: ActivityResultLauncher<Intent?>,
    builder: (Intent.() -> Unit)
) {
    launcher.launch(Intent(this, activityClass).apply(builder))
}

fun <T> Context.startActivity(
    activityClass: Class<T>,
    launcher: ActivityResultLauncher<Intent?>
) {
    launcher.launch(Intent(this, activityClass))
}

fun Activity.restartApp() {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

    startActivity(intent)
    Runtime.getRuntime().exit(0)  // 앱 프로세스 종료
}