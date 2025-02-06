package st.seno.autotrading.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.PermissionChecker
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import st.seno.autotrading.BuildConfig
import timber.log.Timber

object PermissionUtil {

    fun check(context: Context, permission: String): Boolean =
        PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestNotificationPermission(listener: PermissionListener) {
        TedPermission.create()
            .setPermissions(Manifest.permission.POST_NOTIFICATIONS)
            .setPermissionListener(listener)
            .check()
    }

    fun startAppSettings(context: Context) {
        context.startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", BuildConfig.APPLICATION_ID, null))
        )
    }
}