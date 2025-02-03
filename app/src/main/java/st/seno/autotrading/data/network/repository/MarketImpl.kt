package st.seno.autotrading.data.network.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import st.seno.autotrading.data.mapper.MarketCryptoMapper
import st.seno.autotrading.data.network.model.Crypto
import st.seno.autotrading.data.network.model.Result
import st.seno.autotrading.data.network.service.MarketService
import javax.inject.Inject

class MarketImpl @Inject constructor(
    private val service: MarketService,
    private val mapper: MarketCryptoMapper

) : MarketRepository {
    override suspend fun reqMarketCrypto(): Flow<Result<List<Crypto>>> {
        return flow {
            repeat(3) { count ->
                runCatching {
                    val response = service.reqMarketCrypto()
                    emit(Result.Success(response.map { mapper.fromRemote(it) }))
                    return@flow
                }.onFailure {
                    if (count == 2) {
                        emit(Result.Error(it))
                    }
                }
            }
        }
    }
}