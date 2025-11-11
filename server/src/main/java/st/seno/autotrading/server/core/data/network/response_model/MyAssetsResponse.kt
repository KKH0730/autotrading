package st.seno.autotrading.server.core.data.network.response_model

import com.fasterxml.jackson.annotation.JsonProperty


data class MyAssetsResponse(
    @JsonProperty("currency")
    val currency: String, // 화폐를 의미하는 영문 대문자 코드
    @JsonProperty("balance")
    val balance: String, // 주문가능 금액/수량
    @JsonProperty("locked")
    val locked: String, // 주문 중 묶여있는 금액/수량
    @JsonProperty("avg_buy_price")
    val avgBuyPrice: String, // 매수평균가
    @JsonProperty("avg_buy_price_modified")
    val avgBuyPriceModified: Boolean, // 매수평균가 수정 여부
    @JsonProperty("unit_currency")
    val unitCurrency: String // 평단가 기준 화폐
)