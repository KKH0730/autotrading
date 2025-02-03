package st.seno.autotrading.data.network.service

import retrofit2.http.GET
import retrofit2.http.Query
import st.seno.autotrading.data.network.response_model.ClosedOrdersResponse

interface OrderService {
    @GET("orders/closed")
    suspend fun reqClosedOrders(
        @Query("market") marketId: String,
        @Query("states") states: Array<String>,
        @Query("start_time") startTime: String?,
        @Query("end_time") endTime: String,
        @Query("limit") limit: Int
    ): List<ClosedOrdersResponse>
}