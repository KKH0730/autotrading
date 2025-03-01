package st.seno.autotrading.domain

import st.seno.autotrading.data.network.model.ClosedOrder
import st.seno.autotrading.data.network.model.Order
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.repository.OrderRepository
import st.seno.autotrading.data.network.response_model.IndividualOrder
import st.seno.autotrading.extensions.safeCall
import javax.inject.Inject

class OrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
) {
    suspend fun reqOrder(
        marketId: String,
        side: String,
        volume: String?,
        price: String?,
        ordType: String,
    ): Result<Order> = safeCall {
        orderRepository.reqOrder(
            marketId = marketId,
            side = side,
            volume = volume,
            price = price,
            ordType = ordType
        )
    }

    suspend fun reqClosedOrders(
        marketId: String,
        states: Array<String>,
        startTime: String? = null,
        endTime: String,
        limit: Int
    ): Result<List<ClosedOrder>> = safeCall {
        orderRepository.reqClosedOrders(
            marketId = marketId,
            states = states,
            startTime = startTime,
            endTime = endTime,
            limit = limit
        )
    }

    suspend fun reqIndividualOrders(
        uuid: String,
    ): Result<IndividualOrder> = safeCall {
        orderRepository.reqIndividualOrder(uuid = uuid)
    }
}