package st.seno.autotrading.data.mapper

import st.seno.autotrading.data.network.model.Crypto
import st.seno.autotrading.data.network.response_model.MarketCryptoResponse
import javax.inject.Inject

class MarketCryptoMapper @Inject constructor() : Mapper<MarketCryptoResponse, Crypto> {
    override fun fromRemote(model: MarketCryptoResponse): Crypto {
        return with(model) {
            Crypto(
                marketId = this.market,
                koName = this.koreanName,
                enName = this.englishName
            )
        }
    }
}