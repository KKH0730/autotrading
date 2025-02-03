package st.seno.autotrading.data.network.repository

import kotlinx.coroutines.flow.Flow
import st.seno.autotrading.data.network.model.ClosedOrder
import st.seno.autotrading.data.network.model.Result

interface OrderRepository {
    suspend fun reqClosedOrders(
        marketId: String, // 마켓 ID
        states: Array<String>,
        startTime: String?,
        endTime: String,
        limit: Int, // 요청 개수, default: 100, max: 1,000
    ) : Flow<Result<List<ClosedOrder>>>
}