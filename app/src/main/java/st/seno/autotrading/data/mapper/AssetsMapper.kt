package st.seno.autotrading.data.mapper

import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.data.network.response_model.MyAssetsResponse
import javax.inject.Inject

class AssetsMapper @Inject constructor(): Mapper<MyAssetsResponse, Asset> {
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