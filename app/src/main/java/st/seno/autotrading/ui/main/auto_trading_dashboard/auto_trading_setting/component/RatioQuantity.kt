package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF374151
import st.seno.autotrading.theme.FF3B82F6
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FFF3F4F6
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.rationQuantities


@Composable
fun RatioQuantity(
    quantityRatioIndex: Int,
    onClickQuantityRatio: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            stringResource(R.string.quantity),
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = FF374151
            )
        )
        10.HeightSpacer()
        RatioQuantityContainer(
            selectedRatioIndex = quantityRatioIndex,
            onClickQuantityRatio = onClickQuantityRatio
        )
        12.HeightSpacer()
        Text(
            String.format(stringResource(R.string.auto_trading_quantity_balance_s), "${rationQuantities[quantityRatioIndex]}%"),
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Normal,
                color = FF4B5563
            )
        )
    }
}

@Composable
fun RatioQuantityContainer(selectedRatioIndex: Int, onClickQuantityRatio: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        rationQuantities.forEachIndexed { index, value ->
            RationQuantityItem(
                index = index,
                value = value,
                selectedValue = rationQuantities[selectedRatioIndex],
                onClickQuantityRatio = onClickQuantityRatio,
                modifier = Modifier.weight(weight = 1f)
            )
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun RationQuantityItem(
    index: Int,
    value: String,
    selectedValue: String,
    modifier: Modifier,
    onClickQuantityRatio: (Int) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = 8.dp))
            .background(color = if (value == selectedValue) FF3B82F6 else FFF3F4F6)
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) { onClickQuantityRatio.invoke(index) }
    ) {
        Text(
            "$value%",
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = if (value == selectedValue) FFFFFFFF else FF374151
            ),
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}