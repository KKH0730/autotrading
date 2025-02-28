package st.seno.autotrading.data.network.repository

import st.seno.autotrading.data.network.model.Crypto

interface MarketRepository {
    suspend fun reqMarketCrypto(): List<Crypto>
}