package st.seno.autotrading.data.network.service

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import st.seno.autotrading.data.network.response_model.ClosedOrdersResponse
import st.seno.autotrading.data.network.response_model.IndividualOrderResponse
import st.seno.autotrading.data.network.response_model.OrderResponse

interface OrderService {
    /**
     * 주문하기
     **/
    @POST("orders")
    suspend fun reqOrder(
        @Body requestBody: Map<String, String?>
    ): OrderResponse

    /**
     * 체결 대기 주문 조회
     **/
    @GET("orders/closed")
    suspend fun reqClosedOrders(
        @Query("market") marketId: String,
        @Query("states") states: Array<String>,
        @Query("start_time") startTime: String?,
        @Query("end_time") endTime: String,
        @Query("limit") limit: Int
    ): List<ClosedOrdersResponse>

    /**
     * 체결 대기 주문 조회
     **/
    @GET("orders")
    suspend fun reqIndividualOrder(
        @Query("uuid") uuid: String
    ): List<IndividualOrderResponse>


}