package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.extensions.toDate
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF374151
import st.seno.autotrading.theme.FFE5E7EB
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.ui.common.CircleRippleButton
import st.seno.autotrading.ui.common.CommonInputContainer
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.TradeDate
import st.seno.autotrading.ui.main.auto_trading_dashboard.component.CalendarPicker

@Composable
fun ManualModePanel(
    stopLossValue: TextFieldValue,
    takeProfitValue: TextFieldValue,
    startDateValue: Long,
    endDateValue: Long,
    tradeDateValue: TradeDate,
    onStopLossChanged: (TextFieldValue) -> Unit,
    onTakeProfitChanged: (TextFieldValue) -> Unit,
    onClickStartDatePicker: (Long) -> Unit,
    onClickEndDatePicker: (Long) -> Unit,
    onChangeDate: (TradeDate) -> Unit
) {
    Box {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 18.dp)
        ) {
            TradingStrategy()
            CommonInputContainer(
                title = stringResource(R.string.auto_trading_stop_loss_title),
                textValue = stopLossValue,
                keyboardType = KeyboardType.NumberPassword,
                onTextChanged = {
                    val value = if (it.text.isNotEmpty() && it.text.length > 1 && it.text[0] == '0') {
                        it.text.slice(1..<it.text.length)
                    } else {
                        it.text
                    }
                    try {
                        when {
                            value.toInt() > 99 -> onStopLossChanged.invoke(TextFieldValue(text = "99", selection = TextRange(index = "99".length)))
                            else -> onStopLossChanged.invoke(TextFieldValue(text = value, selection = TextRange(index = value.length)))
                        }
                    } catch (e: Exception) {
                        onStopLossChanged.invoke(TextFieldValue(text = value, selection = TextRange(index = value.length)))
                    }
                }
            )
            CommonInputContainer(
                title = stringResource(R.string.auto_trading_take_profit_title),
                textValue = takeProfitValue,
                keyboardType = KeyboardType.NumberPassword,
                onTextChanged = {
                    val value = if (it.text.isNotEmpty() && it.text.length > 1 && it.text[0] == '0') {
                        it.text.slice(1..<it.text.length)
                    } else {
                        it.text
                    }
                    try {
                        when {
                            value.toInt() > 99 -> onTakeProfitChanged.invoke(TextFieldValue(text = "99", selection = TextRange(index = "99".length)))
                            else -> onTakeProfitChanged.invoke(TextFieldValue(text = value, selection = TextRange(index = value.length)))
                        }
                    } catch (e: Exception) {
                        onTakeProfitChanged.invoke(TextFieldValue(text = value, selection = TextRange(index = value.length)))
                    }
                }
            )
            TradingDate(
                date = endDateValue,
                title = stringResource(R.string.auto_trading_end_date),
                onClickDatePicker = onClickEndDatePicker
            )
            24.HeightSpacer()
        }

        if (tradeDateValue.isShowDatePicker) {
            CalendarPicker(
                selectedDate = tradeDateValue.selectedDate,
                onConfirm = {
                    onChangeDate.invoke(TradeDate(isShowDatePicker = false, tradeDateType = tradeDateValue.tradeDateType, selectedDate = it))
                },
                onDismissed = {
                    onChangeDate.invoke(TradeDate(isShowDatePicker = false, tradeDateType = tradeDateValue.tradeDateType, selectedDate = tradeDateValue.selectedDate))
                }
            )
        }
    }
}

//region(TradingStrategy)
@Composable
fun TradingStrategy() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            stringResource(R.string.auto_trading_strategy_title),
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = FF374151
            )
        )
        8.HeightSpacer()
        Card(
            border = BorderStroke(
                width = 1.dp,
                color = FFE5E7EB
            ),
            backgroundColor = FFF9FAFB,
            shape = RoundedCornerShape(size = 8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 40.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    stringResource(R.string.auto_trading_strategy_menu_1),
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF000000
                    ),
                    modifier = Modifier.align(alignment = Alignment.CenterStart)
                )
            }
        }
    }
}
//endregion

//region(TradingDate)
@Composable
fun TradingDate(
    title: String,
    date: Long,
    onClickDatePicker: (Long) -> Unit
) {
    Column {
        Text(
            title,
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = FF374151
            ),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        8.HeightSpacer()
        TradingDateTextPanel(selectedDate = date, onClickDatePicker = onClickDatePicker)
    }
}

@Composable
fun TradingDateTextPanel(
    selectedDate: Long,
    onClickDatePicker: (Long) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = FFE5E7EB
                ),
                shape = RoundedCornerShape(size = 8.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(height = 40.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                selectedDate.toDate(pattern = "yyyy-MM-dd"),
                style = TextStyle(
                    fontSize = 12.textDp,
                    fontWeight = FontWeight.Normal,
                    color = FF000000
                )
            )
            FullWidthSpacer()
            CircleRippleButton(
                size = 24.dp,
                content = { modifier ->
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = null,
                        tint = FF000000,
                        modifier = modifier
                            .size(size = 14.dp)
                    )
                },
                onClick = { onClickDatePicker.invoke(selectedDate) }
            )
        }
    }
}
//endregion