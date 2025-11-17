package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_backtest.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.isNotEmpty
import st.seno.autotrading.extensions.numberWithCommas
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.common.CommonInputContainer
import st.seno.autotrading.ui.common.CryptoDropDown
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.TradeDate
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component.TradingDate
import st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component.TradingStrategy
import st.seno.autotrading.ui.main.auto_trading_dashboard.component.CalendarPicker
import java.util.Calendar

@Composable
fun BackTestSettingsPanel(
    selectedAutoTradingCrypto: String,
    isExpandCryptoDropDownMenu: Boolean,
    bookmarkedTickers: List<String>,
    initialInvestment: TextFieldValue,
    stopLossValue: TextFieldValue,
    takeProfitValue: TextFieldValue,
    correctionValue: TextFieldValue,
    startDateValue: Long,
    endDateValue: Long,
    tradeDateValue: TradeDate,
    onClickCryptoText: () -> Unit,
    onClickCryptoDropdownMenu: (Boolean) -> Unit,
    onClickCryptoDropdownMenuItem: (String) -> Unit,
    onInitialInvestmentChanged: (TextFieldValue) -> Unit,
    onStopLossChanged: (TextFieldValue) -> Unit,
    onTakeProfitChanged: (TextFieldValue) -> Unit,
    onCorrectionValueChanged: (TextFieldValue) -> Unit,
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
            BackTestSettingTitle()
            24.HeightSpacer()
            CryptoDropDown(
                selectedCrypto = selectedAutoTradingCrypto,
                isExpandCryptoDropDownMenu = isExpandCryptoDropDownMenu,
                bookmarkedTickers = bookmarkedTickers,
                onClickCryptoText = onClickCryptoText,
                onClickCryptoDropdownMenu = onClickCryptoDropdownMenu,
                onClickCryptoDropdownMenuItem = onClickCryptoDropdownMenuItem
            )
            18.HeightSpacer()
            TradingStrategy()
            18.HeightSpacer()
            CommonInputContainer(
                title = stringResource(R.string.auto_trading_back_test_price),
                textValue = TextFieldValue(text = initialInvestment.text.numberWithCommas(), selection = TextRange(index = initialInvestment.text.numberWithCommas().length)),
                keyboardType = KeyboardType.NumberPassword,
                onTextChanged = {
                    val text = it.text.replace(",", "")
                    val value = if (text.isNotEmpty() && text.length > 1 && text[0] == '0') {
                        text.slice(1..<text.length)
                    } else {
                        text
                    }
                    onInitialInvestmentChanged.invoke(TextFieldValue(text = value, selection = TextRange(index = value.length)))
                }
            )
            18.HeightSpacer()
            CommonInputContainer(
                title = stringResource(R.string.auto_trading_stop_loss_title),
                textValue = stopLossValue,
                blockText = stringResource(R.string.auto_trading_back_test_block_stop_loss),
                keyboardType = KeyboardType.NumberPassword,
                enabled = (takeProfitValue.text.isNotEmpty() && takeProfitValue.text.toInt() == 0) || takeProfitValue.text.isEmpty(),
                isBlock = takeProfitValue.isNotEmpty() && takeProfitValue.text.toInt() > 0,
                onTextChanged = {
                    val value = if (it.text.isNotEmpty() && it.text.length > 1 && it.text[0] == '0') {
                        it.text.slice(1..<it.text.length).toInt()
                    } else if(it.text.isEmpty()) {
                        0
                    } else {
                        it.text.toInt()
                    }
                    try {
                        when {
                            value > 99 -> onStopLossChanged.invoke(TextFieldValue(text = "99", selection = TextRange(index = "99".length)))
                            else -> onStopLossChanged.invoke(TextFieldValue(text = value.toString(), selection = TextRange(index = value.toString().length)))
                        }
                    } catch (e: Exception) {
                        onStopLossChanged.invoke(TextFieldValue(text = value.toString(), selection = TextRange(index = value.toString().length)))
                    }
                }
            )
            18.HeightSpacer()
            CommonInputContainer(
                title = stringResource(R.string.auto_trading_take_profit_title),
                textValue = takeProfitValue,
                blockText = stringResource(R.string.auto_trading_back_test_block_take_profit),
                keyboardType = KeyboardType.NumberPassword,
                enabled = (stopLossValue.text.isNotEmpty() && stopLossValue.text.toInt() == 0) || stopLossValue.text.isEmpty(),
                isBlock = stopLossValue.isNotEmpty() && stopLossValue.text.toInt() > 0,
                onTextChanged = {
                    val value = if (it.text.isNotEmpty() && it.text.length > 1 && it.text[0] == '0') {
                        it.text.slice(1..<it.text.length).toInt()
                    } else if(it.text.isEmpty()) {
                        0
                    } else {
                        it.text.toInt()
                    }
                    try {
                        when {
                            value > 99 -> onTakeProfitChanged.invoke(TextFieldValue(text = "99", selection = TextRange(index = "99".length)))
                            else -> onTakeProfitChanged.invoke(TextFieldValue(text = value.toString(), selection = TextRange(index = value.toString().length)))
                        }
                    } catch (e: Exception) {
                        onTakeProfitChanged.invoke(TextFieldValue(text = value.toString(), selection = TextRange(index = value.toString().length)))
                    }
                }
            )
            18.HeightSpacer()
            CommonInputContainer(
                title = stringResource(R.string.auto_trading_back_test_correction_value),
                textValue = correctionValue,
                keyboardType = KeyboardType.Number,
                onTextChanged = {
                    if (it.text.toFloatOrNull()?.let { value -> value in 0.0..0.9 } == true) {
                        onCorrectionValueChanged.invoke(TextFieldValue(text = it.text, selection = TextRange(index = it.text.length)))
                    }
                }
            )
            18.HeightSpacer()
            TradingDate(
                date = startDateValue,
                title = stringResource(R.string.auto_trading_start_date),
                onClickDatePicker = onClickStartDatePicker
            )
            18.HeightSpacer()
            TradingDate(
                date = endDateValue,
                title = stringResource(R.string.auto_trading_end_date),
                onClickDatePicker = onClickEndDatePicker
            )
            24.HeightSpacer()
        }
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



// region(Title)
@Composable
fun BackTestSettingTitle() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Text(
            stringResource(R.string.auto_trading_back_test_setting),
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