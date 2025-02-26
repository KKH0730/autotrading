package st.seno.autotrading.ui.main.settings

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FFFFFFFF

@Composable
fun SettingsScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().background(color = FFFFFFFF)
    ) {
        24.HeightSpacer()
        Text(
            "Settings",
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        16.HeightSpacer()
        Card(
            shape = RoundedCornerShape(size = 8.dp),
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 14.dp, horizontal = 8.dp)
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
                Text(
                    "v${getAppVersion(context = context)}",
                    style = TextStyle(
                        fontSize = 10.textDp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                )
            }
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