package st.seno.autotrading.data.network.model

data class Candle(
    val market: String = "", // 종목 코드
    val candleDateTimeUtc: String = "", // 캔들 기준 시각(UTC 기준) 포맷: yyyy-MM-dd HH:mm:ss
    val candleDateTimeKst: String = "", // 캔들 기준 시각(KST 기준) 포맷: yyyy-MM-dd HH:mm:ss
    val openingPrice: Double, // 시가
    val highPrice: Double, // 고가
    val lowPrice: Double, // 저가
    val tradePrice: Double, // 종가
    val timestamp: Long, //마지막 틱이 저장된 시각
    val candleAccTradePrice: Double, // 누적 거래 금액
    val candleAccTradeVolume: Double, // 누적 거래량
    val prevClosingPrice: Double? = null, // 전일 종가(UTC 0시 기준)
    val changePrice: Double? = null, // 전일 종가 대비 변화 금액
    val changeRate: Double? = null, // 전일 종가 대비 변화량
    val convertedTradePrice: Double? = null, // 종가 환산 화폐 단위로 환산된 가격(요청에 convertingPriceUnit 파라미터 없을 시 해당 필드 포함되지 않음.)
    val firstDayOfPeriod: String? = null, // 캔들 기간의 가장 첫 날
    val unit: Int? = null, // 분 단위(유닛)
    var side: String? = null
)