package st.seno.autotrading.data.mapper

import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.response_model.CandleResponse
import javax.inject.Inject

class CandleMapper @Inject constructor() : Mapper<CandleResponse, Candle> {
    override fun fromRemote(model: CandleResponse): Candle {
        return with(model) {
            Candle(
                market = market,
                candleDateTimeUtc = candleDateTimeUtc,
                candleDateTimeKst = candleDateTimeKst,
                openingPrice = openingPrice,
                highPrice = highPrice,
                lowPrice = lowPrice,
                tradePrice = tradePrice,
                timestamp = timestamp,
                candleAccTradePrice = candleAccTradePrice,
                candleAccTradeVolume = candleAccTradeVolume,
                prevClosingPrice = prevClosingPrice,
                changePrice = changePrice,
                changeRate = changeRate,
                convertedTradePrice = convertedTradePrice
            )
        }
    }
}