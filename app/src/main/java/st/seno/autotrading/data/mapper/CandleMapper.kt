package st.seno.autotrading.data.mapper

import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.response_model.DaysCandleResponse
import st.seno.autotrading.data.network.response_model.MinutesCandleResponse
import st.seno.autotrading.data.network.response_model.MonthsCandleResponse
import st.seno.autotrading.data.network.response_model.SecondsCandleResponse
import st.seno.autotrading.data.network.response_model.WeeksCandleResponse
import st.seno.autotrading.data.network.response_model.YearsCandleResponse
import st.seno.autotrading.extensions.kstToKoreanTime
import st.seno.autotrading.extensions.utcToKoreanTime
import javax.inject.Inject

class YearsCandleMapper @Inject constructor() : Mapper<YearsCandleResponse, Candle> {
    override fun fromRemote(model: YearsCandleResponse): Candle {
        return with(model) {
            Candle(
                market = market,
                candleDateTimeUtc = candleDateTimeUtc.utcToKoreanTime(),
                candleDateTimeKst = candleDateTimeKst.kstToKoreanTime(),
                openingPrice = openingPrice,
                highPrice = highPrice,
                lowPrice = lowPrice,
                tradePrice = tradePrice,
                timestamp = timestamp,
                candleAccTradePrice = candleAccTradePrice,
                candleAccTradeVolume = candleAccTradeVolume,
                firstDayOfPeriod = firstDayOfPeriod
            )
        }
    }
}

class MonthsCandleMapper @Inject constructor() : Mapper<MonthsCandleResponse, Candle> {
    override fun fromRemote(model: MonthsCandleResponse): Candle {
        return with(model) {
            Candle(
                market = market,
                candleDateTimeUtc = candleDateTimeUtc.utcToKoreanTime(),
                candleDateTimeKst = candleDateTimeKst.kstToKoreanTime(),
                openingPrice = openingPrice,
                highPrice = highPrice,
                lowPrice = lowPrice,
                tradePrice = tradePrice,
                timestamp = timestamp,
                candleAccTradePrice = candleAccTradePrice,
                candleAccTradeVolume = candleAccTradeVolume,
                firstDayOfPeriod = firstDayOfPeriod
            )
        }
    }
}

class WeeksCandleMapper @Inject constructor() : Mapper<WeeksCandleResponse, Candle> {
    override fun fromRemote(model: WeeksCandleResponse): Candle {
        return with(model) {
            Candle(
                market = market,
                candleDateTimeUtc = candleDateTimeUtc.utcToKoreanTime(),
                candleDateTimeKst = candleDateTimeKst.kstToKoreanTime(),
                openingPrice = openingPrice,
                highPrice = highPrice,
                lowPrice = lowPrice,
                tradePrice = tradePrice,
                timestamp = timestamp,
                candleAccTradePrice = candleAccTradePrice,
                candleAccTradeVolume = candleAccTradeVolume,
                firstDayOfPeriod = firstDayOfPeriod
            )
        }
    }
}

class DaysCandleMapper @Inject constructor() : Mapper<DaysCandleResponse, Candle> {
    override fun fromRemote(model: DaysCandleResponse): Candle {
        return with(model) {
            Candle(
                market = market,
                candleDateTimeUtc = candleDateTimeUtc.utcToKoreanTime(),
                candleDateTimeKst = candleDateTimeKst.kstToKoreanTime(),
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

class MinutesCandleMapper @Inject constructor() : Mapper<MinutesCandleResponse, Candle> {
    override fun fromRemote(model: MinutesCandleResponse): Candle {
        return with(model) {
            Candle(
                market = market,
                candleDateTimeUtc = candleDateTimeUtc.utcToKoreanTime(),
                candleDateTimeKst = candleDateTimeKst.kstToKoreanTime(),
                openingPrice = openingPrice,
                highPrice = highPrice,
                lowPrice = lowPrice,
                tradePrice = tradePrice,
                timestamp = timestamp,
                candleAccTradePrice = candleAccTradePrice,
                candleAccTradeVolume = candleAccTradeVolume,
                unit = unit
            )
        }
    }
}

class SecondsCandleMapper @Inject constructor() : Mapper<SecondsCandleResponse, Candle> {
    override fun fromRemote(model: SecondsCandleResponse): Candle {
        return with(model) {
            Candle(
                market = market,
                candleDateTimeUtc = candleDateTimeUtc.utcToKoreanTime(),
                candleDateTimeKst = candleDateTimeKst.kstToKoreanTime(),
                openingPrice = openingPrice,
                highPrice = highPrice,
                lowPrice = lowPrice,
                tradePrice = tradePrice,
                timestamp = timestamp,
                candleAccTradePrice = candleAccTradePrice,
                candleAccTradeVolume = candleAccTradeVolume,
            )
        }
    }
}