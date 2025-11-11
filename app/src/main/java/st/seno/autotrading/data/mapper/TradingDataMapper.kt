package st.seno.autotrading.data.mapper

import com.google.firebase.firestore.QueryDocumentSnapshot
import st.seno.autotrading.data.network.response_model.IndividualOrder
import st.seno.autotrading.data.network.response_model.TradingData
import st.seno.autotrading.data.network.response_model.TradingHistoryResponse
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.extensions.mapToModel
import st.seno.autotrading.extensions.parseDateFormat
import timber.log.Timber
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TradingDataMapper @Inject constructor() : Mapper<TradingHistoryResponse, List<TradingData>> {
    override fun fromRemote(model: TradingHistoryResponse): List<TradingData> {
        return with(model) {
            this.history?.map { it } ?: listOf()
        }
    }
}