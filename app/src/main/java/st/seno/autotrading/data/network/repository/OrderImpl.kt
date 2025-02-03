package st.seno.autotrading.data.network.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import st.seno.autotrading.data.mapper.ClosedOrdersMapper
import st.seno.autotrading.data.network.model.ClosedOrder
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.service.OrderService
import javax.inject.Inject

class OrderImpl @Inject constructor(
    private val service: OrderService,
    private val closedMapper: ClosedOrdersMapper
) : OrderRepository {
    override suspend fun reqClosedOrders(
        marketId: String,
        states: Array<String>,
        startTime: String?,
        endTime: String,
        limit: Int
    ): Flow<Result<List<ClosedOrder>>> {
        return flow {
            repeat(3) { count ->
                runCatching {
                    val response = service.reqClosedOrders(
                        marketId = marketId,
                        states = states,
                        startTime = startTime,
                        endTime = endTime,
                        limit = limit
                    )
                    emit(Result.Success(response.map { closedMapper.fromRemote(it) }))
                    return@flow
                }.onFailure {
                    if (count == 2) {
                        emit(Result.Error(it))
                    }
                }
            }
        }
    }
}