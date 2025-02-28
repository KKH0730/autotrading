package st.seno.autotrading.domain

import kotlinx.coroutines.CoroutineDispatcher
import st.seno.autotrading.data.network.model.ClosedOrder
import st.seno.autotrading.data.network.model.Order
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.repository.OrderRepository
import st.seno.autotrading.data.network.response_model.IndividualOrder
import st.seno.autotrading.di.Qualifiers
import st.seno.autotrading.extensions.catchError
import javax.inject.Inject

class OrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    @Qualifiers.IoDispatcher val ioDispatcher: CoroutineDispatcher
) {
    suspend fun reqOrder(
        marketId: String,
        side: String,
        volume: String?,
        price: String?,
        ordType: String,
    ): Result<Order> = orderRepository.reqOrder(
        marketId = marketId,
        side = side,
        volume = volume,
        price = price,
        ordType = ordType
    ).catchError(dispatcher = ioDispatcher)

    suspend fun reqClosedOrders(
        marketId: String,
        states: Array<String>,
        startTime: String? = null,
        endTime: String,
        limit: Int
    ): Result<List<ClosedOrder>> = orderRepository.reqClosedOrders(
        marketId = marketId,
        states = states,
        startTime = startTime,
        endTime = endTime,
        limit = limit
    ).catchError(dispatcher = ioDispatcher)

    suspend fun reqIndividualOrders(
        uuid: String,
    ): Result<IndividualOrder> = orderRepository.reqIndividualOrder(uuid = uuid).catchError(dispatcher = ioDispatcher)
}