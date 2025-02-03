package st.seno.autotrading.data.network.model

import com.google.gson.annotations.SerializedName

data class Ticker (
    @SerializedName("cd")
    val code: String, // 마켓 코드 (ex. KRW-BTC)
    @SerializedName("op")
    val openingPrice: Double, // 시가
    @SerializedName("hp")
    val highPrice: Double, // 고가
    @SerializedName("lp")
    val lowPrice: Double, // 저가
    @SerializedName("tp")
    val tradePrice: Double, // 현재가
    @SerializedName("pcp")
    val prevClosingPrice: Double, // 전일종가
    @SerializedName("c")
    val change: String, // 전일대비(RISE : 상승, EVEN : 보합, FALL : 하락)
    @SerializedName("cp")
    val changePrice: Double, // 부호 없는 전일 대비 값
    @SerializedName("scp")
    val signedChangePrice: Double, // 일 대비 값
    @SerializedName("cr")
    val changeRate: Double, // 부호 없는 전일 대비 등락율
    @SerializedName("scr")
    val signedChangeRate: Double, // 전일 대비 등락율
    @SerializedName("tv")
    val tradeVolume: Double, // 가장 최근 거래량
    @SerializedName("atv")
    val accTradeVolume: Double, // 누적 거래량(UTC 0시 기준)
    @SerializedName("atv24h")
    val accTradeVolume24h: Double, // 24시간 누적 거래량
    @SerializedName("atp")
    val accTradePrice: Double, // 누적 거래대금(UTC 0시 기준)
    @SerializedName("atp24h")
    val accTradePrice24h: Double, // 24시간 누적 거래대금
    @SerializedName("tdt")
    val tradeDate: String, // 최근 거래 일자(UTC) (ex yyyyMMdd)
    @SerializedName("ttm")
    val tradeTime: String, // 최근 거래 시각(UTC) (ex HHmmss)
    @SerializedName("ttms")
    val tradeTimestamp: Long, // 체결 타임스탬프 (milliseconds)
    @SerializedName("ab")
    val askBid: String, // 매수/매도 구분(ASK : 매도, BID : 매수)
    @SerializedName("aav")
    val accAskVolume: Double, // 누적 매도량
    @SerializedName("abv")
    val accBidVolume: Double, // 누적 매수량
    @SerializedName("h52wp")
    val highest52WeekPrice: Double, // 52주 최고가
    @SerializedName("h52wdt")
    val highest52WeekDate: String, // 52주 최고가 달성일 (ex yyyy-MM-dd)
    @SerializedName("l52wp")
    val lowest52WeekPrice: Double, // 52주 최저가
    @SerializedName("l52wdt")
    val lowest52WeekDate: String, // 52주 최저가 달성일 (ex yyyy-MM-dd)
    @SerializedName("mw")
    val marketWarning: String, // 유의 종목 여부(NONE : 해당없음, CAUTION : 투자유의)
    @SerializedName("tms")
    val timestamp: Long, // 타임스탬프 (millisecond)
    @SerializedName("st")
    val streamType: String, // 스트림 타입(SNAPSHOT : 스냅샷, REALTIME : 실시간)
)