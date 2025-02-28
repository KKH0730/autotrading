package st.seno.autotrading.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
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
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.bitcoin_jumping)
    )
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = FFCEDBDA)
        ) {
            LottieAnimation(
                composition = composition,
                iterations = Int.MAX_VALUE,
                speed = 2f,
                modifier = Modifier
                    .height(height = 150.dp)
                    .width(width = 300.dp)
                    .align(alignment = Alignment.Center)
            )
        }
    } else {
        RestartDialog(
            title = context.getString(R.string.unknown_error_title),
            content = context.getString(R.string.unknown_error_content),
            confirmText = context.getString(R.string.alert_dialog_restart),
            onClickConfirm = { (context as SplashActivity).restartApp() }
        )
    }
}