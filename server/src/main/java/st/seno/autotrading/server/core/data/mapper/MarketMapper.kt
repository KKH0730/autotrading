package st.seno.autotrading.server.core.data.mapper

import org.springframework.stereotype.Component
import st.seno.autotrading.server.core.data.network.model.Crypto
import st.seno.autotrading.server.core.data.network.response_model.MarketCryptoResponse

@Component
class MarketCryptoMapper() : Mapper<MarketCryptoResponse, Crypto> {
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