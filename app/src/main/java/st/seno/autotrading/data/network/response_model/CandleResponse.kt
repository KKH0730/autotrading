package st.seno.autotrading.data.network.response_model

import com.google.gson.annotations.SerializedName

data class CandleResponse(
    @SerializedName("market")
    val market: String, // 종목 코드
    @SerializedName("candle_date_time_utc")
    val candleDateTimeUtc: String, // 캔들 기준 시각(UTC 기준) 포맷: yyyy-MM-dd'T'HH:mm:ss
    @SerializedName("candle_date_time_kst")
    val candleDateTimeKst: String, // 캔들 기준 시각(KST 기준) 포맷: yyyy-MM-dd'T'HH:mm:ss
    @SerializedName("opening_price")
    val openingPrice: Double, // 시가
    @SerializedName("high_price")
    val highPrice: Double, // 고가
    @SerializedName("low_price")
    val lowPrice: Double, // 저가
    @SerializedName("trade_price")
    val tradePrice: Double, // 종가
    @SerializedName("timestamp")
    val timestamp: Long, //마지막 틱이 저장된 시각
    @SerializedName("candle_acc_trade_price")
    val candleAccTradePrice: Double, // 누적 거래 금액
    @SerializedName("candle_acc_trade_volume")
    val candleAccTradeVolume: Double, // 누적 거래량
    @SerializedName("prev_closing_price")
    val prevClosingPrice: Double, // 전일 종가(UTC 0시 기준)
    @SerializedName("change_price")
    val changePrice: Double, // 전일 종가 대비 변화 금액
    @SerializedName("change_rate")
    val changeRate: Double, // 전일 종가 대비 변화량
    @SerializedName("converted_trade_price")
    val convertedTradePrice: Double // 종가 환산 화폐 단위로 환산된 가격(요청에 convertingPriceUnit 파라미터 없을 시 해당 필드 포함되지 않음.)
)