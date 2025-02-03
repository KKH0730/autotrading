import android.annotation.SuppressLint
import android.view.*
import android.view.WindowInsets.Type.systemBars
import androidx.core.view.WindowInsetsControllerCompat

@SuppressLint("WrongConstant")
fun Window.hideNavigationBar() {
    setDecorFitsSystemWindows(false)

    val insetsControllerCompat = WindowInsetsControllerCompat(this, this.decorView)
    insetsControllerCompat.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    insetsControllerCompat.hide(systemBars())
}