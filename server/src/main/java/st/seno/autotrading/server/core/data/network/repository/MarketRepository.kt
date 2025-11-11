package st.seno.autotrading.server.core.data.network.repository

import org.springframework.stereotype.Repository
import st.seno.autotrading.server.core.data.network.model.Crypto

@Repository
interface MarketRepository {
    suspend fun reqMarketCrypto(): List<Crypto>
}