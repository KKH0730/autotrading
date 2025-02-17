package st.seno.autotrading.data.network.model

import com.google.gson.annotations.SerializedName

data class RealtimeCandle(
    @SerializedName("ty")
    val type: String, // 타입
    @SerializedName("cd")
    val code: String, // 마켓 코드 (ex. KRW-BTC)
    @SerializedName("cdttmu")
    val candleDateTimeUtc: String, // 캔들 기준 시각(UTC 기준) 포맷: 포맷: yyyy-MM-dd'T'HH:mm:ss
    @SerializedName("cdttmk")
    val candleDateTimeKst: String, // 캔들 기준 시각(KST 기준) 포맷: yyyy-MM-dd'T'HH:mm:ss
    @SerializedName("op")
    val openingPrice: Double, // 시가
    @SerializedName("hp")
    val highPrice: Double, // 고가
    @SerializedName("lp")
    val lowPrice: Double, // 저가
    @SerializedName("tp")
    val tradPrice: Double, // 종가
    @SerializedName("catv")
    val candleAccTradeVolume: Double, // 누적 거래 금액
    @SerializedName("catp")
    val candleAccTradePrice: Double, // 누적 거래량
    @SerializedName("tms")
    val timestamp: Long, // 타임스탬프 (millisecond)
    @SerializedName("st")
    val streamType: String // 스트림 타입
)