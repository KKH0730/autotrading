package st.seno.autotrading.server.core.data.mapper


import org.springframework.stereotype.Component
import st.seno.autotrading.server.core.data.network.model.Candle
import st.seno.autotrading.server.core.data.network.response_model.DaysCandleResponse
import st.seno.autotrading.server.core.data.network.response_model.MinutesCandleResponse
import st.seno.autotrading.server.core.data.network.response_model.MonthsCandleResponse
import st.seno.autotrading.server.core.data.network.response_model.SecondsCandleResponse
import st.seno.autotrading.server.core.data.network.response_model.WeeksCandleResponse
import st.seno.autotrading.server.core.data.network.response_model.YearsCandleResponse
import st.seno.autotrading.server.core.extension.parseDateFormat
import java.time.format.DateTimeFormatter

@Component
class YearsCandleMapper() : Mapper<YearsCandleResponse, Candle> {
    override fun fromRemote(model: YearsCandleResponse): Candle {
        return with(model) {
            Candle(
                market = market,
                candleDateTimeUtc = candleDateTimeUtc.parseDateFormat(
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ),
                candleDateTimeKst = candleDateTimeKst.parseDateFormat(
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ),
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

@Component
class MonthsCandleMapper() : Mapper<MonthsCandleResponse, Candle> {
    override fun fromRemote(model: MonthsCandleResponse): Candle {
        return with(model) {
            Candle(
                market = market,
                candleDateTimeUtc = candleDateTimeUtc.parseDateFormat(
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ),
                candleDateTimeKst = candleDateTimeKst.parseDateFormat(
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ),
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

@Component
class WeeksCandleMapper() : Mapper<WeeksCandleResponse, Candle> {
    override fun fromRemote(model: WeeksCandleResponse): Candle {
        return with(model) {
            Candle(
                market = market,
                candleDateTimeUtc = candleDateTimeUtc.parseDateFormat(
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ),
                candleDateTimeKst = candleDateTimeKst.parseDateFormat(
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ),
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

@Component
class DaysCandleMapper() : Mapper<DaysCandleResponse, Candle> {
    override fun fromRemote(model: DaysCandleResponse): Candle {
        return with(model) {
            Candle(
                market = market,
                candleDateTimeUtc = candleDateTimeUtc.parseDateFormat(
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ),
                candleDateTimeKst = candleDateTimeKst.parseDateFormat(
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ),
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

@Component
class MinutesCandleMapper() : Mapper<MinutesCandleResponse, Candle> {
    override fun fromRemote(model: MinutesCandleResponse): Candle {
        return with(model) {
            Candle(
                market = market,
                candleDateTimeUtc = candleDateTimeUtc.parseDateFormat(
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ),
                candleDateTimeKst = candleDateTimeKst.parseDateFormat(
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ),
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

@Component
class SecondsCandleMapper() : Mapper<SecondsCandleResponse, Candle> {
    override fun fromRemote(model: SecondsCandleResponse): Candle {
        return with(model) {
            Candle(
                market = market,
                candleDateTimeUtc = candleDateTimeUtc.parseDateFormat(
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ),
                candleDateTimeKst = candleDateTimeKst.parseDateFormat(
                    inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ),
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