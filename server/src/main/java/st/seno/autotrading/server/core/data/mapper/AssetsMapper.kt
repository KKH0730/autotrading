package st.seno.autotrading.server.core.data.mapper

import org.springframework.stereotype.Component
import st.seno.autotrading.server.core.data.network.model.Asset
import st.seno.autotrading.server.core.data.network.response_model.MyAssetsResponse

@Component
class AssetsMapper(): Mapper<MyAssetsResponse, Asset> {
    override fun fromRemote(model: MyAssetsResponse): Asset {
        return with(model) {
            Asset(
                currency = this.currency,
                balance = this.balance,
                lockedBalance = this.locked,
                avgBuyPrice = this.avgBuyPrice,
                unitCurrency = this.unitCurrency
            )
        }
    }
}