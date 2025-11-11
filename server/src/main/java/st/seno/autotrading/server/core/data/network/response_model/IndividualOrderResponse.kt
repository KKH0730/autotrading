package st.seno.autotrading.server.core.data.network.response_model

import com.fasterxml.jackson.annotation.JsonProperty

data class IndividualOrderResponse(
    @JsonProperty("uuid")
    val uuid: String, // 주문의 고유 아이디
    @JsonProperty("side")
    val side: String, // 주문 종류
    @JsonProperty("ord_type")
    val ordType: String, // 주문 방식
    @JsonProperty("price")
    val price: String?, // 주문 당시 화폐 가격
    @JsonProperty("state")
    val state: String, // 주문 상태
    @JsonProperty("market")
    val market: String, // 마켓의 유일키
    @JsonProperty("created_at")
    val createdAt: String, // 주문 생성 시간
    @JsonProperty("volume")
    val volume: String?, // 사용자가 입력한 주문 양
    @JsonProperty("remaining_volume")
    val remainingVolume: String?, // 체결 후 남은 주문 양
    @JsonProperty("reserved_fee")
    val reservedFee: String, // 수수료로 예약된 비용
    @JsonProperty("remaining_fee")
    val remainingFee: String, // 남은 수수료
    @JsonProperty("paid_fee")
    val paidFee: String, // 사용된 수수료
    @JsonProperty("locked")
    val locked: String, // 거래에 사용중인 비용
    @JsonProperty("executed_volume")
    val executedVolume: String, // 체결된 양
    @JsonProperty("trades_count")
    val tradesCount: Int, // 해당 주문에 걸린 체결 수
    @JsonProperty("trades")
    val trades: List<TradeResponse>?, // 체결
    @JsonProperty("time_in_force")
    val timeInForce: String?, // IOC, FOK 설정
    @JsonProperty("identifier")
    val identifier: String? // 조회용 사용자 지정값
)

data class TradeResponse(
    @JsonProperty("market")
    val tradesMarket: String, // 마켓의 유일 키
    @JsonProperty("uuid")
    val tradesUuid: String, // 체결의 고유 이아디
    @JsonProperty("price")
    val tradesPrice: String, // 체결 가격
    @JsonProperty("volume")
    val tradesVolume: String, // 체결 양
    @JsonProperty("funds")
    val tradesFunds: String, // 체결된 총 가격
    @JsonProperty("side")
    val tradesSide: String, // 체결 종류
    @JsonProperty("created_at")
    val tradesCreatedAt: String // 체결 시각
)