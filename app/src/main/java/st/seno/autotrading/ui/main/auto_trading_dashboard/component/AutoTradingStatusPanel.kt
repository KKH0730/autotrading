package st.seno.autotrading.ui.main.auto_trading_dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import st.seno.autotrading.R
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF2563EB
import st.seno.autotrading.theme.FFDC2626
import st.seno.autotrading.theme.FFEFF6FF
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.theme.FFFEF2F2
import st.seno.autotrading.theme.FFFFFFFF

@Composable
fun AutoTradingStatusPanel(
    isRunningAutoTradingService: Boolean,
    onClickStopTrading: () -> Unit,
    onClickViewTrading: () -> Unit,
    onClickStartAutoTrading: () -> Unit
) {
    Card(
        elevation = 1.dp,
        backgroundColor = FFFFFFFF,
        shape = RoundedCornerShape(size = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            16.HeightSpacer()
            AutoTradingStatusTitle()
            AutoTradingStatus(isActivated = isRunningAutoTradingService)
            AutoTradingControlPanel(
                isRunningAutoTradingService = isRunningAutoTradingService,
                onClickStopTrading = onClickStopTrading,
                onClickViewTrading = onClickViewTrading,
                onClickStartAutoTrading = onClickStartAutoTrading
            )
            16.HeightSpacer()
        }
    }
}

@Composable
fun AutoTradingStatusTitle() {
    Row(verticalAlignment = Alignment.CenterVertically,) {
        Text(
            stringResource(R.string.auto_trading_dashboard_trade_status),
            style = TextStyle(
                fontSize = 17.textDp,
                fontWeight = FontWeight.SemiBold,
                color = FF000000
            ),
            overflow = TextOverflow.Ellipsis,
        )
        this.FullWidthSpacer()
    }
}

@Composable
fun AutoTradingStatus(isActivated: Boolean) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.trading_ing)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 50.dp)
            .background(color = FFF9FAFB)
    ) {
        if (isActivated) {
            LottieAnimation(
                modifier = Modifier.size(50.dp),
                composition = composition,
                iterations = Int.MAX_VALUE
            )
        }
        8.WidthSpacer()
        Text(
            if (isActivated) stringResource(R.string.auto_trading_status_on) else stringResource(R.string.auto_trading_status_off),
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = FF000000
            ),
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun AutoTradingControlPanel(
    isRunningAutoTradingService: Boolean,
    onClickStopTrading: () -> Unit,
    onClickViewTrading: () -> Unit,
    onClickStartAutoTrading: () -> Unit
) {
    if (isRunningAutoTradingService) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
            modifier = Modifier.height(height = 50.dp)
        ) {
            AutoTradingControlButton(
                name = stringResource(R.string.auto_trading_stop),
                backgroundColor = FFFEF2F2,
                textColor = FFDC2626,
                image = painterResource(R.drawable.ic_stop_trading),
                iconTint = FFDC2626,
                modifier = Modifier.weight(weight = 1f),
                onClick = onClickStopTrading
            )
            16.WidthSpacer()
            AutoTradingControlButton(
                name = stringResource(R.string.auto_trading_view),
                backgroundColor = FFEFF6FF,
                textColor = FF2563EB,
                image = painterResource(R.drawable.ic_auto_trading_view),
                iconTint = FF2563EB,
                modifier = Modifier.weight(weight = 1f),
                onClick = onClickViewTrading
            )
        }
    } else {
        AutoTradingControlButton(
            name = stringResource(R.string.auto_trading_start_setting),
            backgroundColor = FF2563EB,
            textColor = FFFFFFFF,
            image = painterResource(R.drawable.ic_setting_lines),
            iconTint = FFFFFFFF,
            modifier = Modifier.fillMaxWidth(),
            onClick = onClickStartAutoTrading
        )
    }
}

@Composable
fun AutoTradingControlButton(
    name: String,
    backgroundColor: Color,
    textColor: Color,
    image: Painter,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
        ),
        modifier = modifier.height(height = 40.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = image,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(size = 14.dp)
            )
            12.WidthSpacer()
            Text(
                name,
                style = TextStyle(
                    fontSize = 12.textDp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                ),
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}