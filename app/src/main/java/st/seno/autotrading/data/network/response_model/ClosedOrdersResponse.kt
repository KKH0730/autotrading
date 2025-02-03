package st.seno.autotrading.data.network.response_model

import com.google.gson.annotations.SerializedName

data class ClosedOrdersResponse(
    @SerializedName("uuid")
    val uuid: String, // 주문의 고유 아이디
    @SerializedName("side")
    val side: String, // 주문 종류
    @SerializedName("ord_type")
    val ordType: String, // 주문 방식(limit : 지정가 주문, price : 시장가 주문(매수) market : 시장가 주문(매도) best : 최유리 주문)
    @SerializedName("price")
    val price: String, // 주문 당시 화폐 가격
    @SerializedName("state")
    val state: String, // 주문 상태
    @SerializedName("market")
    val market: String, // 마켓 ID
    @SerializedName("created_at")
    val createdAt: String, // 주문 생성 시각
    @SerializedName("volume")
    val volume: String, // 사용자가 입력한 주문 양
    @SerializedName("remaining_volume")
    val remainingVolume: String, // 체결 후 남은 주문 양
    @SerializedName("reserved_fee")
    val reservedFee: String, // 수수료로 예약된 비용
    @SerializedName("remaining_fee")
    val remainingFee: String, // 남은 수수료
    @SerializedName("paid_fee")
    val paidFee: String, // 사용된 수수료
    @SerializedName("locked")
    val locked: String, // 거래에 사용 중인 비용
    @SerializedName("executed_volume")
    val executedVolume: String, // 체결된 양
    @SerializedName("executed_funds")
    val executedFunds: String, // 현재까지 체결된 금액
    @SerializedName("trades_count")
    val tradesCount: Int, // 해당 주문에 걸린 체결 수
    @SerializedName("time_in_force")
    val timeInForce: String, // IOC, FOK 설정(ioc : Immediate or Cancel, fok : Fill or Kill)
    @SerializedName("identifier")
    val identifier: String // 조회용 사용자 지정값(*identifier 필드는 2024-10-18 이후에 생성된 주문에 대해서만 제공)
)