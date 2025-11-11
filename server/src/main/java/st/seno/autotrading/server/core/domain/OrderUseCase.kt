package st.seno.autotrading.server.core.domain

import org.springframework.stereotype.Service
import st.seno.autotrading.server.core.data.network.model.ClosedOrder
import st.seno.autotrading.server.core.data.network.model.Order
import st.seno.autotrading.server.core.data.network.repository.OrderRepository
import st.seno.autotrading.server.core.data.network.model.IndividualOrder
import st.seno.autotrading.server.core.extension.safeCall
import st.seno.autotrading.server.core.data.network.model.Result

@Service
class OrderUseCase(
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