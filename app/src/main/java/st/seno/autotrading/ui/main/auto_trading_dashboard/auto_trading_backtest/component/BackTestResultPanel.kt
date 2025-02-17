package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_backtest.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.doubleWithCommas
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.model.BackTestResult
import st.seno.autotrading.model.PriceTrend
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF2563EB
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FFE5E7EB
import st.seno.autotrading.theme.FFFFFFFF

@Composable
fun BackTestResultPanel(
    backTestResult: BackTestResult,
    onClickConfirm: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 0.dp, bottom = 40.dp)
    ) {
        BackTestResultCard(
            isNormalCard = true,
            title = stringResource(R.string.auto_trading_back_test_result_total_profit),
            value = backTestResult.totalProfit.toString(),
            priceTrend = if (backTestResult.totalProfit > 0.0) PriceTrend.RISE else if (backTestResult.totalProfit < 0.0) PriceTrend.FALL else PriceTrend.EVEN,
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(space = 16.dp)) {
            BackTestResultCard(
                title = stringResource(R.string.auto_trading_back_test_result_total_return_rate),
                value = backTestResult.totalReturnRate.toString(),
                priceTrend = if (backTestResult.totalReturnRate > 0.0) PriceTrend.RISE else if (backTestResult.totalReturnRate < 0.0) PriceTrend.FALL else PriceTrend.EVEN,
                modifier = Modifier.weight(weight = 1f)
            )
            BackTestResultCard(
                isWindRateCard = true,
                title = stringResource(R.string.auto_trading_back_test_result_win_rate),
                value = backTestResult.winRate.toString(),
                priceTrend = if (backTestResult.winRate > 0.0) PriceTrend.RISE else if (backTestResult.winRate < 0.0) PriceTrend.FALL else PriceTrend.EVEN,
                modifier = Modifier.weight(weight = 1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(space = 16.dp)) {
            BackTestResultCard(
                title = stringResource(R.string.auto_trading_back_test_result_max_profit),
                value = backTestResult.maximumProfitRate.toString(),
                priceTrend = if (backTestResult.maximumProfitRate > 0.0) PriceTrend.RISE else if (backTestResult.maximumProfitRate < 0.0) PriceTrend.FALL else PriceTrend.EVEN,
                modifier = Modifier.weight(weight = 1f)
            )
            BackTestResultCard(
                title = stringResource(R.string.auto_trading_back_test_result_max_loss),
                value = backTestResult.maximumLossRate.toString(),
                priceTrend = if (backTestResult.maximumLossRate > 0.0) PriceTrend.RISE else if (backTestResult.maximumLossRate < 0.0) PriceTrend.FALL else PriceTrend.EVEN,
                modifier = Modifier.weight(weight = 1f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(space = 16.dp)) {
            BackTestResultCard(
                isNormalCard = true,
                title = stringResource(R.string.auto_trading_back_test_result_total_fee),
                value = backTestResult.totalFee.toString(),
                priceTrend = if (backTestResult.totalProfit > 0.0) PriceTrend.RISE else if (backTestResult.totalProfit < 0.0) PriceTrend.FALL else PriceTrend.EVEN,
                modifier = Modifier.weight(weight = 1f)
            )
            BackTestResultCard(
                isNormalCard = true,
                title = stringResource(R.string.auto_trading_back_test_result_trade_count),
                value = backTestResult.tradeCount.toString(),
                priceTrend = if (backTestResult.totalProfit > 0.0) PriceTrend.RISE else if (backTestResult.totalProfit < 0.0) PriceTrend.FALL else PriceTrend.EVEN,
                modifier = Modifier.weight(weight = 1f)
            )
        }

        BacTestResultConfirmButton(onClickConfirm = onClickConfirm)
    }
}

@Composable
fun BackTestResultCard(
    isNormalCard: Boolean = false,
    isWindRateCard: Boolean = false,
    title: String,
    value: String,
    priceTrend: PriceTrend,
    modifier: Modifier,
) {
    Card (
        backgroundColor = FFFFFFFF,
        shape = RoundedCornerShape(size = 10.dp),
        elevation = 1.dp,
        border = BorderStroke(
            width = 1.dp,
            color = FFE5E7EB
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 16.dp)
        ) {
            Text(
                title,
                style = TextStyle(
                    fontSize = 13.textDp,
                    fontWeight = FontWeight.Normal,
                    color = FF4B5563
                )
            )
            5.HeightSpacer()
            Text(
                if (isNormalCard) {
                    value.doubleWithCommas()
                } else if (isWindRateCard) {
                    "$value%"
                } else {
                    if (priceTrend == PriceTrend.RISE) "+${value}%" else if (priceTrend == PriceTrend.FALL) "${value}%" else "$value%"
                },
                style = TextStyle(
                    fontSize = 17.textDp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isNormalCard || isWindRateCard) {
                        FF000000
                    } else {
                        priceTrend.color
                    }
                )
            )
        }
    }
}

@Composable
fun BacTestResultConfirmButton(
    onClickConfirm: () -> Unit
) {
    Button(
        onClick = onClickConfirm,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = FF2563EB,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 40.dp)
    ) {
        Text(
            stringResource(R.string.confirm),
            style = TextStyle(
                fontSize = 14.textDp,
                fontWeight = FontWeight.Medium,
                color = FFFFFFFF
            ),
            overflow = TextOverflow.Ellipsis,
        )
    }
}