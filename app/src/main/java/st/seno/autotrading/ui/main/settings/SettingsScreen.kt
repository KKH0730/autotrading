package st.seno.autotrading.ui.main.settings

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.startActivity
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF16A34A
import st.seno.autotrading.theme.FFDC2626
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.main.settings.color_preference.ColorPreferenceActivity
import timber.log.Timber

@Composable
fun SettingsScreen() {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize().background(color = FFFFFFFF)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Text(
                "Settings",
                style = TextStyle(
                    fontSize = 24.textDp,
                    fontWeight = FontWeight.Bold,
                    color = FF000000
                )
            )
            10.HeightSpacer()
            SettingPanel(
                title = "Appearance",
                content = {
                    SettingColorPreference(
                        onClick = {
                            context.startActivity(ColorPreferenceActivity::class.java)
                        }
                    )
                }
            )
            SettingPanel(
                title = "Version",
                content = {
                    SettingItem(
                        value = "v${getAppVersion(context = context)}",
                        painter = null
                    )
                }
            )
        }
    }
}

@Composable
fun SettingPanel(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        SettingItemTitle(title = title)
        content()
    }
}

@Composable
fun SettingItemTitle(title: String) {
    Text(
        title,
        style = TextStyle(
            fontSize = 10.textDp,
            fontWeight = FontWeight.Normal,
            color = Color.Gray
        )
    )
}

@Composable
fun SettingItem(
    value: String? = null,
    painter: Painter? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 14.dp)
    ) {
        Text(
            "Current version",
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = FF000000
            )
        )
        FullWidthSpacer()
        value?.let {
            Text(
                it,
                style = TextStyle(
                    fontSize = 10.textDp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            )
        }
        painter?.let {
            Image(
                it,
                contentDescription = null,
                modifier = Modifier.size(size = 16.dp)
            )
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun SettingColorPreference(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 14.dp)
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource(),
                onClick = onClick
            )
    ) {
        Text(
            "Color Preference",
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = FF000000
            )
        )
        FullWidthSpacer()
        Row {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = FF16A34A,
                modifier = Modifier
                    .size(size = 12.dp)
                    .rotate(degrees = -90f)
            )
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = FFDC2626,
                modifier = Modifier
                    .size(size = 12.dp)
                    .rotate(degrees = 90f)
            )
            16.WidthSpacer()
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .size(size = 16.dp)
            )
        }
    }
}

fun getAppVersion(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName
    } catch (e: Exception) {
        ""
    }
}