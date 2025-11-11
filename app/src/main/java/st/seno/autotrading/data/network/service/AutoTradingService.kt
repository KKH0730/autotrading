package st.seno.autotrading.data.network.service

import retrofit2.http.Body
import retrofit2.http.POST
import st.seno.autotrading.data.network.response_model.ServiceResponse
import st.seno.autotrading.data.network.response_model.TradingHistoryResponse

interface AutoTradingService {

    @POST("/autotrading/start/trading")
    suspend fun startTrading(@Body body: Map<String, @JvmSuppressWildcards Any?>): ServiceResponse

    @POST("/autotrading/stop/trading")
    suspend fun stopAutoTrading(@Body body: Map<String, String>): ServiceResponse

    @POST("/autotrading/check/trading")
    suspend fun checkAutoTrading(@Body body: Map<String, String>): ServiceResponse

    @POST("/autotrading/trading/history")
    suspend fun getTradingHistory(@Body body: Map<String, String>): TradingHistoryResponse
}