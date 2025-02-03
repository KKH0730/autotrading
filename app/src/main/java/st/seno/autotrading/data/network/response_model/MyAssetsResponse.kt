package st.seno.autotrading.data.network.response_model

import com.google.gson.annotations.SerializedName

data class MyAssetsResponse(
    @SerializedName("currency")
    val currency: String, // 화폐를 의미하는 영문 대문자 코드
    @SerializedName("balance")
    val balance: String, // 주문가능 금액/수량
    @SerializedName("locked")
    val locked: String, // 주문 중 묶여있는 금액/수량
    @SerializedName("avg_buy_price")
    val avgBuyPrice: String, // 매수평균가
    @SerializedName("avg_buy_price_modified")
    val avgBuyPriceModified: Boolean, // 매수평균가 수정 여부
    @SerializedName("unit_currency")
    val unitCurrency: String // 평단가 기준 화폐
)