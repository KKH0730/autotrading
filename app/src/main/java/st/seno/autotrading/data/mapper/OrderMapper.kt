package st.seno.autotrading.data.mapper

import st.seno.autotrading.data.network.model.ClosedOrder
import st.seno.autotrading.data.network.response_model.ClosedOrdersResponse
import javax.inject.Inject

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