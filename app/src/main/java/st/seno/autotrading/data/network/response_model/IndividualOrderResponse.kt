package st.seno.autotrading.data.network.response_model

import com.google.gson.annotations.SerializedName

data class IndividualOrderResponse(
    @SerializedName("uuid")
    val uuid: String, // 주문의 고유 아이디
    @SerializedName("side")
    val side: String, // 주문 종류
    @SerializedName("ord_type")
    val ordType: String, // 주문 방식
    @SerializedName("price")
    val price: String?, // 주문 당시 화폐 가격
    @SerializedName("state")
    val state: String, // 주문 상태
    @SerializedName("market")
    val market: String, // 마켓의 유일키
    @SerializedName("created_at")
    val createdAt: String, // 주문 생성 시간
    @SerializedName("volume")
    val volume: String?, // 사용자가 입력한 주문 양
    @SerializedName("remaining_volume")
    val remainingVolume: String?, // 체결 후 남은 주문 양
    @SerializedName("reserved_fee")
    val reservedFee: String, // 수수료로 예약된 비용
    @SerializedName("remaining_fee")
    val remainingFee: String, // 남은 수수료
    @SerializedName("paid_fee")
    val paidFee: String, // 사용된 수수료
    @SerializedName("locked")
    val locked: String, // 거래에 사용중인 비용
    @SerializedName("executed_volume")
    val executedVolume: String, // 체결된 양
    @SerializedName("trades_count")
    val tradesCount: Int, // 해당 주문에 걸린 체결 수
    @SerializedName("trades")
    val trades: List<TradeResponse>?, // 체결
    @SerializedName("time_in_force")
    val timeInForce: String?, // IOC, FOK 설정
    @SerializedName("identifier")
    val identifier: String? // 조회용 사용자 지정값
)

data class TradeResponse(
    @SerializedName("market")
    val tradesMarket: String, // 마켓의 유일 키
    @SerializedName("uuid")
    val tradesUuid: String, // 체결의 고유 이아디
    @SerializedName("price")
    val tradesPrice: String, // 체결 가격
    @SerializedName("volume")
    val tradesVolume: String, // 체결 양
    @SerializedName("funds")
    val tradesFunds: String, // 체결된 총 가격
    @SerializedName("side")
    val tradesSide: String, // 체결 종류
    @SerializedName("created_at")
    val tradesCreatedAt: String, // 체결 시각
)