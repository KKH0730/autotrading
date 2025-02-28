package st.seno.autotrading.data.network.repository

import st.seno.autotrading.data.network.model.ClosedOrder
import st.seno.autotrading.data.network.model.Order
import st.seno.autotrading.data.network.response_model.IndividualOrder

interface OrderRepository {
    /**
     * 시장가 주문(매수)
     * ordType: price
     * side: bid
     * volume: null
     * price: 필수 입력
     *
     * 시장가 주문(매도)
     * ordType: market
     * side: ask
     * volume: 필수 입력
     * price: null
     *
     * 지정가 주문
     * ordType: limit
     * side: bid or ask
     * volume: 필수 입력
     * price: 필수 입력
     */
    suspend fun reqOrder(
        marketId: String, // 마켓 ID
        side: String, // 주문 종류(bid: 매수, ask: 매도)
        volume: String? = null, // 주문량 (지정가, 시장가 매도 시 필수)
        price: String? = null, // 주문 가격(지정가, 시장가 매수 시 필수)
        ordType: String, // 주문 타입(limit: 지정가 주문, price: 시장가 주문(매수), market: 시장가 주문(매도), best: 최유리 주문(time_in_force 설정 필수))
    ): Order

    suspend fun reqClosedOrders(
        marketId: String, // 마켓 ID
        states: Array<String>,
        startTime: String?,
        endTime: String,
        limit: Int, // 요청 개수, default: 100, max: 1,000
    ): List<ClosedOrder>

    suspend fun reqIndividualOrder(
        uuid: String
    ): IndividualOrder

}