package st.seno.autotrading.data.network.model

data class Asset(
    val currency: String, // 화폐를 의미하는 영문 대문자 코드(ex: BTC, XRP, USDT, KRW)
    var balance: String, // 주문가능 금액/수량
    val lockedBalance: String, // 주문 중 묶여있는 금액/수량
    val avgBuyPrice: String, // 매수평균가
    val unitCurrency: String, // 평단가 기준 화폐
    var tradePrice: Double = 0.0, // 현재가
    var evaluatedPrice: Double = 0.0, // 평가금액
    var signedChangeRate: Double = 0.0, // 현재가와 평균가 대비 등락율
)