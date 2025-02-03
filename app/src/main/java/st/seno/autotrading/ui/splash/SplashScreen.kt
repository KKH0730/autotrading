package st.seno.autotrading.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import st.seno.autotrading.R
import st.seno.autotrading.extensions.restartApp
import st.seno.autotrading.theme.FFCEDBDA
import st.seno.autotrading.ui.common.RestartDialog
import st.seno.autotrading.ui.main.MainActivity

@Composable
fun SplashScreen(
    startMain: Boolean,
    onFinished: () -> Unit,

) {
    var countState by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(countState) {
        if (startMain && countState > 2) {
            onFinished.invoke()
            return@LaunchedEffect
        }

        delay(1000)
        countState += 1
    }
    if (startMain || countState <= 2) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = FFCEDBDA)
        ) {
            Spacer(modifier = Modifier.height(height = 210.dp))
            Image(
                painter = painterResource(id = R.drawable.img_splash),
                contentDescription = null,
                modifier = Modifier.width(width = 216.dp)
                    .aspectRatio(ratio = 2.37f)
            )
            Spacer(modifier = Modifier.weight(weight = 1f))
        }
    } else {
        RestartDialog(
            title = context.getString(R.string.unknown_error_title),
            content = context.getString(R.string.unknown_error_content),
            confirmText = context.getString(R.string.alert_dialog_restart),
            onClickConfirm = { (context as SplashActivity).restartApp() }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = FFCEDBDA)
        ) {
            Spacer(modifier = Modifier.height(height = 210.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_setting),
                contentDescription = null,
                modifier = Modifier.width(width = 216.dp)
                    .aspectRatio(ratio = 2.37f)
            )
            Spacer(modifier = Modifier.weight(weight = 1f))
        }
    }
}