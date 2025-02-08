package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.getString
import st.seno.autotrading.extensions.pxToDp
import st.seno.autotrading.extensions.screenWidth
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF374151
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FFF3F4F6
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.common.CryptoDropDown
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.TradeDate

@Composable
fun AutoTradingSettingPanel(
    selectedAutoTradingCrypto: String,
    isExpandCryptoDropDownMenu: Boolean,
    bookmarkedTickers: List<String>,
    quantityRatioIndex: Int,
    currentTradingMode: String,
    stopLossValue: TextFieldValue,
    takeProfitValue: TextFieldValue,
    startDateValue: Long,
    endDateValue: Long,
    tradeDateValue: TradeDate,
    onClickCryptoText: () -> Unit,
    onClickCryptoDropdownMenu: (Boolean) -> Unit,
    onClickCryptoDropdownMenuItem: (String) -> Unit,
    onClickQuantityRatio: (Int) -> Unit,
    onClickTradingMode: (String) -> Unit,
    onStopLossChanged: (TextFieldValue) -> Unit,
    onTakeProfitChanged: (TextFieldValue) -> Unit,
    onClickStartDatePicker: (Long) -> Unit,
    onClickEndDatePicker: (Long) -> Unit,
    onChangeDate: (TradeDate) -> Unit
) {
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(size = 8.dp),
        backgroundColor = FFFFFFFF,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Column {
            24.HeightSpacer()
            AutoTradingSettingTitle()
            25.HeightSpacer()
            CryptoDropDown(
                selectedCrypto = selectedAutoTradingCrypto,
                isExpandCryptoDropDownMenu = isExpandCryptoDropDownMenu,
                bookmarkedTickers = bookmarkedTickers,
                onClickCryptoText = onClickCryptoText,
                onClickCryptoDropdownMenu = onClickCryptoDropdownMenu,
                onClickCryptoDropdownMenuItem = onClickCryptoDropdownMenuItem
            )
            25.HeightSpacer()
            RatioQuantity(
                quantityRatioIndex = quantityRatioIndex,
                onClickQuantityRatio = onClickQuantityRatio
            )
            30.HeightSpacer()
            TradingModeSegmentControl(
                currentTradingMode = currentTradingMode,
                onClickTradingMode = onClickTradingMode
            )
            20.HeightSpacer()
            if (currentTradingMode == tradeModes[0]) {
                ManualModePanel(
                    stopLossValue = stopLossValue,
                    takeProfitValue = takeProfitValue,
                    startDateValue = startDateValue,
                    endDateValue = endDateValue,
                    tradeDateValue = tradeDateValue,
                    onStopLossChanged = onStopLossChanged,
                    onTakeProfitChanged = onTakeProfitChanged,
                    onClickStartDatePicker = onClickStartDatePicker,
                    onClickEndDatePicker = onClickEndDatePicker,
                    onChangeDate = onChangeDate,
                )
            } else {
                AiTradingModelPanel()
            }
        }
    }
}

// region(Title)
@Composable
fun AutoTradingSettingTitle() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Text(
            stringResource(R.string.auto_trading_setting_title),
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
// endregion

//region(TradingMode)
val tradeModes = listOf(getString(R.string.auto_trading_trading_mode_manual), getString(R.string.auto_trading_trading_mode_ai))

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun TradingModeSegmentControl(
    currentTradingMode: String,
    onClickTradingMode: (String) -> Unit
) {
    val movingOffset by animateDpAsState(
        targetValue = if (currentTradingMode == tradeModes[0]) 4.dp else (screenWidth.pxToDp().dp - 88.dp) / 2,
        label = ""
    )

    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Text(
            stringResource(R.string.auto_trading_trading_mode_title),
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = FF374151
            ),
        )
        8.HeightSpacer()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 44.dp)
                .clip(shape = RoundedCornerShape(size = 8.dp))
                .background(color = FFF3F4F6)
        ) {
            TradingModeSegmentIndicator(
                modifier = Modifier
                    .offset(x = movingOffset)
                    .clip(shape = RoundedCornerShape(size = 8.dp))
                    .align(alignment = Alignment.CenterStart)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                tradeModes.forEach { value ->
                    TradingModeItem(
                        name = value,
                        isSelected = value == currentTradingMode,
                        modifier = Modifier.weight(weight = 1f),
                        onClick = onClickTradingMode
                    )
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun TradingModeItem(
    name: String,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) {
                onClick.invoke(name)
            }
    ) {
        Text(
            name,
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) FF000000 else FF4B5563,
            ),
            modifier = Modifier.align(alignment = Alignment.Center)
        )
    }

}

@Composable
fun TradingModeSegmentIndicator(modifier: Modifier) {
    Box(
        modifier = modifier
            .height(height = 36.dp)
            .width(width = (screenWidth.pxToDp().dp - 112.dp) / 2)
            .background(color = FFFFFFFF)
    )
}
//endregion