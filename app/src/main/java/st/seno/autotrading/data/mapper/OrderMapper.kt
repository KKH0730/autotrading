package st.seno.autotrading.data.mapper

import st.seno.autotrading.data.network.model.ClosedOrder
import st.seno.autotrading.data.network.model.Order
import st.seno.autotrading.data.network.response_model.ClosedOrdersResponse
import st.seno.autotrading.data.network.response_model.IndividualOrder
import st.seno.autotrading.data.network.response_model.IndividualOrderResponse
import st.seno.autotrading.data.network.response_model.OrderResponse
import st.seno.autotrading.data.network.response_model.Trade
import javax.inject.Inject

class OrderMapper @Inject constructor() : Mapper<OrderResponse, Order> {
    override fun fromRemote(model: OrderResponse): Order {
        return with(model) {
            Order(
                uuid = uuid,
                side = side,
                ordType = ordType,
                price = price,
                state = state,
                market = market,
                createdAt = createdAt,
                volume = volume,
                remainingVolume = remainingVolume,
                reservedFee = reservedFee,
                remainingFee = remainingFee,
                paidFee = paidFee,
                locked = locked,
                executedVolume = executedVolume,
                tradesCount = tradesCount,
                timeInForce = timeInForce,
                identifier = identifier
            )
        }
    }
}

class ClosedOrdersMapper @Inject constructor() : Mapper<ClosedOrdersResponse, ClosedOrder> {
    override fun fromRemote(model: ClosedOrdersResponse): ClosedOrder {
        return with(model) {
            ClosedOrder(
                uuid = this.uuid,
                side = this.side,
                ordType = this.ordType,
                price = this.price,
                state = this.state,
                market = this.market,
                createdAt = this.createdAt,
                volume = this.volume,
                remainingVolume = this.remainingVolume,
                reservedFee = this.reservedFee,
                remainingFee = this.remainingFee,
                paidFee = this.paidFee,
                locked = this.locked,
                executedVolume = this.executedVolume,
                executedFunds = this.executedFunds,
                tradesCount = this.tradesCount
            )
        }
    }
}

class IndividualOrderMapper @Inject constructor() :
    Mapper<IndividualOrderResponse, IndividualOrder> {
    override fun fromRemote(model: IndividualOrderResponse): IndividualOrder {
        return with(model) {
            IndividualOrder(
                uuid = uuid,
                side = side,
                ordType = ordType,
                price = price,
                state = state,
                market = market,
                createdAt = createdAt,
                volume = volume,
                remainingVolume = remainingVolume,
                reservedFee = reservedFee,
                remainingFee = remainingFee,
                paidFee = paidFee,
                locked = locked,
                executedVolume = executedVolume,
                tradesCount = tradesCount,
                trades = trades?.map {
                    Trade(
                        tradesMarket = it.tradesMarket,
                        tradesUuid =  it.tradesUuid,
                        tradesPrice = it.tradesPrice,
                        tradesVolume = it.tradesVolume,
                        tradesFunds = it.tradesFunds,
                        tradesSide = it.tradesSide,
                        tradesCreatedAt = it.tradesCreatedAt
                    )
                },
                timeInForce = timeInForce,
                identifier = identifier
            )
        }
    }
}