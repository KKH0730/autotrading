package st.seno.autotrading.ui.main.auto_trading_dashboard.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.ClosedOrder
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.formatPrice
import st.seno.autotrading.extensions.parseDateFormat
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.extensions.toDate
import st.seno.autotrading.prefs.PrefsManager
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF16A34A
import st.seno.autotrading.theme.FF2563EB
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FF626975
import st.seno.autotrading.theme.FF6B7280
import st.seno.autotrading.theme.FFDC2626
import st.seno.autotrading.theme.FFE5E7EB
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.ui.common.CircleRippleButton
import st.seno.autotrading.ui.common.CircleRippleNotFormatSizedButton
import java.util.Calendar

@Composable
fun TradingHistoryPanel(
    selectedCrypto: String,
    selectedDate: Long,
    tradingHistories: List<ClosedOrder>?,
    onClickDropdownMenuItem: (String) -> Unit,
    onSelectedDate: (Long) -> Unit,
    onClickApplyFilter: () -> Unit
) {
    Card(
        backgroundColor = FFFFFFFF,
        elevation = 1.dp,
        shape = RoundedCornerShape(size = 8.dp),
        contentColor = FFFFFFFF,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Column {
            24.HeightSpacer()
            TradeHistoryTitle()
            16.HeightSpacer()
            TradeHistorySettingsPanel(
                selectedCrypto = selectedCrypto,
                selectedDate = selectedDate,
                onClickDropdownMenuItem = onClickDropdownMenuItem,
                onSelectedDate = onSelectedDate,
                onClickApplyFilter = onClickApplyFilter
            )
            if (tradingHistories.isNullOrEmpty()) {
                24.HeightSpacer()
                TradeHistoryEmptyList(tradingHistories = tradingHistories)
                24.HeightSpacer()
            } else {
                TradeHistoryList(tradingHistories = tradingHistories)
            }
        }
    }
}

@Composable
fun TradeHistoryTitle() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Text(
            stringResource(R.string.auto_trading_dashboard_trade_history),
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
fun TradeHistorySettingsPanel(
    selectedCrypto: String,
    selectedDate: Long,
    onClickDropdownMenuItem: (String) -> Unit,
    onSelectedDate: (Long) -> Unit,
    onClickApplyFilter: () -> Unit
) {
    var isShowDatePicker by remember { mutableStateOf(false) }
    var isExpandDropDownMenu by remember { mutableStateOf(false) }

    Column {
        Box {
            SelectedCryptoTextPanel(
                selectedCrypto = selectedCrypto,
                onClick = { isExpandDropDownMenu = !isExpandDropDownMenu }
            )
            CryptoDropDownMenu(
                isExpandDropDownMenu = isExpandDropDownMenu,
                onClickDropdownMenu = { isExpandDropDownMenu = it },
                onClickDropdownMenuItem = onClickDropdownMenuItem
            )
        }
        5.HeightSpacer()
        SelectDateTextPanel(
            selectedDate = selectedDate,
            onClickDatePicker = { isShowDatePicker = true }
        )
        24.HeightSpacer()
        FilterApplyButton(onClick = onClickApplyFilter)
        if (isShowDatePicker) {
            CalendarPicker(
                selectedDate = selectedDate,
                onConfirm = {
                    isShowDatePicker = false
                    onSelectedDate.invoke(it)
                },
                onDismissed = { isShowDatePicker = false }
            )
        }
    }
}

@Composable
fun SelectedCryptoTextPanel(
    selectedCrypto: String,
    onClick: () -> Unit
) {

    val cryptoDisplayName = selectedCrypto.split("-").let { if (it.size <= 1) it[0] else "${it[1]}/${it[0]}"}

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
            .clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.padding(all = 16.dp)) {
            Text(
                cryptoDisplayName,
                style = TextStyle(
                    fontSize = 14.textDp,
                    fontWeight = FontWeight.Normal,
                    color = FF000000
                )
            )
            FullWidthSpacer()
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = FF000000,
                modifier = Modifier
                    .size(size = 14.dp)
                    .rotate(degrees = 90f)
            )
        }
    }
}

@Composable
fun CryptoDropDownMenu(
    isExpandDropDownMenu: Boolean,
    onClickDropdownMenu: (Boolean) -> Unit,
    onClickDropdownMenuItem: (String) -> Unit
) {
    DropdownMenu(
        expanded = isExpandDropDownMenu,
        onDismissRequest = { onClickDropdownMenu.invoke(false) },
        scrollState = rememberScrollState(),
        offset = DpOffset(x = 24.dp, y = 0.dp),
        modifier = Modifier
            .width(width = 200.dp)
            .height(height = 300.dp)
    ) {
        PrefsManager.marketIdList.split(",").forEach { marketId ->
            val marketIdInfos = marketId.split("-")

            DropdownMenuItem(
                text = {
                    Text(
                        "${marketIdInfos[1]}/${marketIdInfos[0]}",
                        style = TextStyle(
                            fontSize = 14.textDp,
                            fontWeight = FontWeight.Normal,
                            color = FF000000
                        )
                    )
                },
                onClick = {
                    onClickDropdownMenu.invoke(false)
                    onClickDropdownMenuItem.invoke("${marketIdInfos[1]}/${marketIdInfos[0]}")
                }
            )
        }
    }
}

@Composable
fun SelectDateTextPanel(
    selectedDate: Long,
    onClickDatePicker: () -> Unit
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
        Row(modifier = Modifier.padding(all = 16.dp)) {
            Text(
                selectedDate.toDate(pattern = "yyyy-MM-dd"),
                style = TextStyle(
                    fontSize = 14.textDp,
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
                onClick = onClickDatePicker
            )
        }
    }
}

@Composable
fun FilterApplyButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = FF2563EB,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 40.dp)
            .padding(horizontal = 24.dp)
    ) {
        Text(
            stringResource(R.string.auto_trading_filter_apply),
            style = TextStyle(
                fontSize = 14.textDp,
                fontWeight = FontWeight.Medium,
                color = FFFFFFFF
            ),
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun TradeHistoryEmptyList(tradingHistories: List<ClosedOrder>?) {
    Column {
        if (tradingHistories != null) {
            50.HeightSpacer()
            Text(
                stringResource(R.string.auto_trading_not_found_trade_history),
                style = TextStyle(
                    fontSize = 13.textDp,
                    fontWeight = FontWeight.Normal,
                    color = FF4B5563
                ),
                textAlign = TextAlign.Center,
                lineHeight = 20.textDp,
                modifier = Modifier.fillMaxWidth()
            )
            24.HeightSpacer()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TradeHistoryList(tradingHistories: List<ClosedOrder>) {
    /**
     * 160: item 높이
     * 16: LazyColumn의 item 간격
     */
    val height = ((tradingHistories.size * 150) + ((tradingHistories.size - 1) * 16) + 48).dp
    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            contentPadding = PaddingValues(vertical = 24.dp),
            userScrollEnabled = false,
            modifier = Modifier.height(height = height)
        ) {
            items(
                count = tradingHistories.size,
            ) { index ->
                TradeInfo(closedOrder = tradingHistories[index])
            }
        }
    }
}

@Composable
fun TradeInfo(closedOrder: ClosedOrder) {
    val executedFunds = closedOrder.executedFunds
    val executedVolume = closedOrder.executedVolume
    val isCanceledOrder =
        executedFunds == null || executedVolume == null || (executedFunds == "0" && executedVolume == "0")

    Card(
        backgroundColor = FFFFFFFF,
        elevation = 1.dp,
        shape = RoundedCornerShape(size = 8.dp),
        contentColor = FFFFFFFF,
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 150.dp)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (isCanceledOrder) stringResource(R.string.auto_trading_order_cancel) else stringResource(
                        R.string.auto_trading_order_done
                    ),
                    style = TextStyle(
                        fontSize = 14.textDp,
                        fontWeight = FontWeight.Medium,
                        color = FF000000
                    )
                )
                FullWidthSpacer()
            }
            12.HeightSpacer()
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (closedOrder.side == stringResource(R.string.auto_trading_ask).lowercase()) {
                        stringResource(R.string.auto_trading_ask)
                    } else {
                        stringResource(R.string.auto_trading_bid)
                    },
                    style = TextStyle(
                        fontSize = 14.textDp,
                        fontWeight = FontWeight.Medium,
                        color = if (closedOrder.side == "ask") FFDC2626 else FF16A34A
                    )
                )
                FullWidthSpacer()
                Text(
                    closedOrder.createdAt?.parseDateFormat() ?: "-",
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF6B7280
                    )
                )
            }
            5.HeightSpacer()
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.auto_trading_market_id),
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF6B7280
                    )
                )
                FullWidthSpacer()
                Text(
                    closedOrder.market ?: "-",
                    style = TextStyle(
                        fontSize = 14.textDp,
                        fontWeight = FontWeight.Medium,
                        color = FF000000
                    )
                )
            }
            5.HeightSpacer()
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.auto_trading_order_type), // limit: 지정가, price: 시장가(매수), market: 시장가(매도), best: 최유리 주문
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF6B7280
                    )
                )
                FullWidthSpacer()
                Text(
                    closedOrder.ordType ?: "-",
                    style = TextStyle(
                        fontSize = 13.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF626975
                    )
                )
            }
            if (!isCanceledOrder) {
                5.HeightSpacer()
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.auto_trading_market_price), // 체결가격
                        style = TextStyle(
                            fontSize = 12.textDp,
                            fontWeight = FontWeight.Normal,
                            color = FF6B7280
                        )
                    )
                    FullWidthSpacer()
                    Text(
                        ((closedOrder.executedFunds?.toDouble()
                            ?: 0.0) / (closedOrder.executedVolume?.toDouble()
                            ?: 0.0)).formatPrice(),
                        style = TextStyle(
                            fontSize = 13.textDp,
                            fontWeight = FontWeight.Normal,
                            color = FF626975
                        )
                    )
                }
            }
            5.HeightSpacer()
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.auto_trading_execute_volume), // 체결된 양
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF6B7280
                    )
                )
                FullWidthSpacer()
                Text(
                    closedOrder.executedVolume ?: "-",
                    style = TextStyle(
                        fontSize = 13.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF626975
                    )
                )
            }
            5.HeightSpacer()
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.auto_trading_execute_funds), // 현재까지 체결된 금액
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF6B7280
                    )
                )
                FullWidthSpacer()
                Text(
                    closedOrder.executedFunds ?: "-",
                    style = TextStyle(
                        fontSize = 13.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF626975
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPicker(
    selectedDate: Long,
    onConfirm: (Long) -> Unit,
    onDismissed: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        yearRange = 1900..Calendar.getInstance().get(Calendar.YEAR),
        initialDisplayMode = DisplayMode.Picker,
        initialSelectedDateMillis = selectedDate
    )

    DatePickerDialog(
        onDismissRequest = onDismissed,
        confirmButton = {
            CircleRippleNotFormatSizedButton(
                onClick = { datePickerState.selectedDateMillis?.let { onConfirm.invoke(it) } },
                content = { modifier ->
                    Text(
                        stringResource(R.string.confirm),
                        style = TextStyle(
                            fontSize = 16.textDp,
                            fontWeight = FontWeight.Normal,
                            color = FF000000
                        ),
                        modifier = modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                    )
                }
            )
        },
        dismissButton = {
            CircleRippleNotFormatSizedButton(
                onClick = onDismissed,
                content = { modifier ->
                    Text(
                        stringResource(R.string.cancel),
                        style = TextStyle(
                            fontSize = 16.textDp,
                            fontWeight = FontWeight.Normal,
                            color = FF000000
                        ),
                        modifier = modifier
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    )
                }
            )
        },
        colors = DatePickerDefaults.colors(containerColor = FFFFFFFF),
        shape = RoundedCornerShape(12.dp)
    ) {
        DatePicker(
            state = datePickerState,
            title = { Text("") },
            colors = DatePickerDefaults.colors(
                containerColor = FFFFFFFF,
                todayContentColor = FF2563EB,
                todayDateBorderColor = FF2563EB,
                selectedDayContainerColor = FF2563EB
            )
        )
    }
}