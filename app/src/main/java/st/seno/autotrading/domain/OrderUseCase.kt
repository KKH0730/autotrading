package st.seno.autotrading.domain

import com.keytalkai.lewis.di.Qualifiers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import st.seno.autotrading.data.network.model.ClosedOrder
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.repository.OrderRepository
import st.seno.autotrading.extensions.catchError
import javax.inject.Inject

class OrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    @Qualifiers.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun reqClosedOrders(
        marketId: String,
        states: Array<String>,
        startTime: String? = null,
        endTime: String,
        limit: Int
    ): Flow<Result<List<ClosedOrder>>> = orderRepository.reqClosedOrders(
        marketId = marketId,
        states = states,
        startTime = startTime,
        endTime = endTime,
        limit = limit
    ).catchError(dispatcher = ioDispatcher)
}