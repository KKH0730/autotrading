package st.seno.autotrading.server.core.data.network.repository

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import st.seno.autotrading.server.core.data.mapper.ClosedOrdersMapper
import st.seno.autotrading.server.core.data.mapper.IndividualOrderMapper
import st.seno.autotrading.server.core.data.mapper.OrderMapper
import st.seno.autotrading.server.core.data.network.model.ClosedOrder
import st.seno.autotrading.server.core.data.network.model.IndividualOrder
import st.seno.autotrading.server.core.data.network.model.Order
import st.seno.autotrading.server.core.data.network.response_model.ClosedOrdersResponse
import st.seno.autotrading.server.core.data.network.response_model.IndividualOrderResponse
import st.seno.autotrading.server.core.data.network.response_model.OrderResponse

@Repository
class OrderImpl(
    private val webClient: WebClient,
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
        val map = mutableMapOf(
            "market" to marketId,
            "side" to side,
            "ord_type" to ordType
        )
        price?.let { map["price"] = it }
        volume?.let { map["volume"] = it }

        val jsonBody = map.entries.joinToString(
                prefix = "{", postfix = "}"
        ) { "\"${it.key}\":\"${it.value}\"" }

        println("jsonBody : $jsonBody")

        val response = webClient.post()
            .uri("/orders")
            .attribute("bodyString", jsonBody) // authFilter에서 읽음
            .bodyValue(jsonBody)
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<OrderResponse>() {})
            .awaitSingle()

        return orderMapper.fromRemote(model = response)
    }

    override suspend fun reqClosedOrders(
        marketId: String,
        states: Array<String>,
        startTime: String?,
        endTime: String,
        limit: Int
    ): List<ClosedOrder> {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/orders/closed")
                    .queryParam("market", marketId)
                    .queryParam("states", states)
                    .queryParam("start_time", startTime)
                    .queryParam("end_time", endTime)
                    .queryParam("limit", limit)
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<ClosedOrdersResponse>>() {})
            .awaitSingle()

        return response.map { closedMapper.fromRemote(it) }
    }

    override suspend fun reqIndividualOrder(uuid: String): IndividualOrder {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/order")
                    .queryParam("uuid", uuid)
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<IndividualOrderResponse>() {})
            .awaitSingle()

        return individualOrderMapper.fromRemote(response)
    }
}