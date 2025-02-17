package st.seno.autotrading.ui.main.auto_trading_dashboard.component

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.noRippleClickable
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FFFFFFFF

@Composable
fun BackTestPanel(
    onClickBackTestSetting: () -> Unit
) {
    Card(
        elevation = 1.dp,
        backgroundColor = FFFFFFFF,
        shape = RoundedCornerShape(size = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        BackTestButton(onClickBackTestSetting = onClickBackTestSetting)
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun BackTestButton(onClickBackTestSetting: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 24.dp)
            .noRippleClickable { onClickBackTestSetting.invoke() }
    ) {
        Text(
            stringResource(R.string.auto_trading_dashboard_back_test),
            style = TextStyle(
                fontSize = 16.textDp,
                fontWeight = FontWeight.SemiBold,
                color = FF000000
            ),
            overflow = TextOverflow.Ellipsis,
        )
        this.FullWidthSpacer()
        Icon(
            Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = FF000000,
            modifier = Modifier.size(size = 20.dp)
        )
    }
}