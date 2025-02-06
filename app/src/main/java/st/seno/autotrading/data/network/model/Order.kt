package st.seno.autotrading.data.network.model

data class Order(
    val uuid: String, // 주문의 고유 아이디
    val side: String, // 주문 종류
    val ordType: String, // 주문 방식
    val price: String?, // 주문 당시 화폐 가격
    val state: String, // 주문 상태
    val market: String, // 마켓의 유일키
    val createdAt: String, // 주문 생성 시간
    val volume: String?, // 사용자가 입력한 주문 양
    val remainingVolume: String?, // 체결 후 남은 주문 양
    val reservedFee: String, //수수료로 얘약된 비용
    val remainingFee: String, // 남은 수수료
    val paidFee: String, // 사용된 수수료
    val locked: String, // 거래에 사용중인 비용
    val executedVolume: String, // 체결된 양
    val tradesCount: Int, // 해당 주문에 걸린 체결 수
    val timeInForce: String?, // IOC, FOK 설정
    val identifier: String?, // 조회용 사용자 지정값
)