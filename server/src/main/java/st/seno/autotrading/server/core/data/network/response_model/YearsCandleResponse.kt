package st.seno.autotrading.server.core.data.network.response_model

import com.fasterxml.jackson.annotation.JsonProperty

data class YearsCandleResponse(
    @JsonProperty("market")
    val market: String, // 종목 코드
    @JsonProperty("candle_date_time_utc")
    val candleDateTimeUtc: String, // 캔들 기준 시각(UTC 기준) 포맷: yyyy-MM-dd'T'HH:mm:ss
    @JsonProperty("candle_date_time_kst")
    val candleDateTimeKst: String, // 캔들 기준 시각(KST 기준) 포맷: yyyy-MM-dd'T'HH:mm:ss
    @JsonProperty("opening_price")
    val openingPrice: Double, // 시가
    @JsonProperty("high_price")
    val highPrice: Double, // 고가
    @JsonProperty("low_price")
    val lowPrice: Double, // 저가
    @JsonProperty("trade_price")
    val tradePrice: Double, // 종가
    @JsonProperty("timestamp")
    val timestamp: Long, //마지막 틱이 저장된 시각
    @JsonProperty("candle_acc_trade_price")
    val candleAccTradePrice: Double, // 누적 거래 금액
    @JsonProperty("candle_acc_trade_volume")
    val candleAccTradeVolume: Double, // 누적 거래량
    @JsonProperty("first_day_of_period")
    val firstDayOfPeriod: String, // 캔들 기간의 가장 첫 날
)