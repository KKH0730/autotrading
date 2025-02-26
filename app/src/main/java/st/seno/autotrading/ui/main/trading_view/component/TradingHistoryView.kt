package st.seno.autotrading.ui.main.trading_view.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import st.seno.autotrading.data.network.response_model.Trade
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.formatRealPrice
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.model.Side
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF047857
import st.seno.autotrading.theme.FF059669
import st.seno.autotrading.theme.FF374151
import st.seno.autotrading.theme.FF475569
import st.seno.autotrading.theme.FFB91C1C
import st.seno.autotrading.theme.FFD1FAE5
import st.seno.autotrading.theme.FFDC2626
import st.seno.autotrading.theme.FFF1F5F9
import st.seno.autotrading.theme.FFF8FAFC
import st.seno.autotrading.theme.FFFEE2E2

//region(TradingHistoryView)
@Composable
fun TradingHistoryView(tradesListHeightState: Float, trades: List<Trade>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(tradesListHeightState.dp)
    ) {
        Text(
            stringResource(R.string.trading_view_recent_trading_history),
            style = TextStyle(
                fontSize = 15.textDp,
                fontWeight = FontWeight.SemiBold,
                color = FF000000
            ),
            modifier = Modifier.padding(start = 20.dp)
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(space = 12.dp),
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                count = trades.size
            ) { index ->
                TradingHistoryItemView(trade = trades[index])
            }
        }
    }
}
//endregion

//region(TradingHistoryItemView)
@Composable
fun TradingHistoryItemView(trade: Trade) {
    val cryptoName = trade.tradesMarket.split("-").takeIf { it.size == 2 }?.let { it[1] } ?: ""
    Card(
        border = BorderStroke(
            width = 1.dp,
            color = FFF8FAFC
        ),
        elevation = 0.dp,
        shape = RoundedCornerShape(size = 8.dp),
        backgroundColor = FFF1F5F9
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 13.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(size = 4.dp))
                        .background(color = if (trade.tradesSide == Side.BID.value) FFD1FAE5 else FFFEE2E2)
                ) {
                    Text(
                        trade.tradesSide,
                        style = TextStyle(
                            fontSize = 12.textDp,
                            fontWeight = FontWeight.Medium,
                            color = if (trade.tradesSide == Side.BID.value) FF047857 else FFB91C1C
                        ),
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp)
                    )
                }
                Text(
                    String.format(
                        stringResource(R.string.s_won),
                        trade.tradesPrice.toDouble().formatRealPrice()
                    ),
                    style = TextStyle(
                        fontSize = 15.textDp,
                        fontWeight = FontWeight.Bold,
                        color = if (trade.tradesSide == Side.BID.value) FF059669 else FFDC2626
                    ),
                )
                FullWidthSpacer()
                Text(
                    "${trade.tradesVolume} $cryptoName",
                    style = TextStyle(
                        fontSize = 14.textDp,
                        fontWeight = FontWeight.Medium,
                        color = FF475569
                    ),
                )
            }
            Text(
                trade.tradesCreatedAt,
                style = TextStyle(
                    fontSize = 14.textDp,
                    fontWeight = FontWeight.Normal,
                    color = FF374151
                ),
                modifier = Modifier.align(alignment = Alignment.End)
            )
        }
    }
}