package st.seno.autotrading.data.network.repository

import st.seno.autotrading.data.mapper.ClosedOrdersMapper
import st.seno.autotrading.data.mapper.IndividualOrderMapper
import st.seno.autotrading.data.mapper.OrderMapper
import st.seno.autotrading.data.network.model.ClosedOrder
import st.seno.autotrading.data.network.model.Order
import st.seno.autotrading.data.network.response_model.IndividualOrder
import st.seno.autotrading.data.network.service.OrderService
import javax.inject.Inject

class OrderImpl @Inject constructor(
    private val service: OrderService,
    private val orderMapper: OrderMapper,
    private val closedMapper: ClosedOrdersMapper,
    private val individualOrderMapper: IndividualOrderMapper
) : OrderRepository {

    override suspend fun reqOrder(
        marketId: String,
        side: String,
        volume: String?,
        price: String?,
        ordType: String
    ): Order {
        val response = service.reqOrder(
            mapOf(
                "market" to marketId,
                "side" to side,
                "volume" to volume,
                "price" to price,
                "ord_type" to ordType,
            )
        )
        return orderMapper.fromRemote(model = response)
    }

    override suspend fun reqClosedOrders(
        marketId: String,
        states: Array<String>,
        startTime: String?,
        endTime: String,
        limit: Int
    ): List<ClosedOrder> {
        val response = service.reqClosedOrders(
            marketId = marketId,
            states = states,
            startTime = startTime,
            endTime = endTime,
            limit = limit
        )
        return response.map { closedMapper.fromRemote(it) }
    }

    override suspend fun reqIndividualOrder(uuid: String): IndividualOrder {
        val response = service.reqIndividualOrder(uuid = uuid)
        return individualOrderMapper.fromRemote(response)
    }
}