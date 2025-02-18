package st.seno.autotrading.data.mapper

import com.google.firebase.firestore.QueryDocumentSnapshot
import st.seno.autotrading.data.network.response_model.TradingData
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.extensions.mapToModel
import javax.inject.Inject

class TradingDataMapper @Inject constructor() : Mapper<QueryDocumentSnapshot, TradingData> {
    override fun fromRemote(model: QueryDocumentSnapshot): TradingData {
        return with(model) {
            TradingData(
                uuid = getString("uuid") ?: "",
                quantityRatio = getDouble("quantityRatio")?.toInt() ?: 0,
                tradingStrategy = getString("tradingStrategy") ?: "",
                stopLoss = getDouble("stopLoss")?.toInt() ?: 0,
                takeProfit = getDouble("takeProfit")?.toInt() ?: 0,
                correctionValue = getDouble("correctionValue")?.toFloat() ?: 0f,
                endDateTime = getString("endDateTime") ?: "",
                order = gson.mapToModel(this.get("order") as Map<String, Any?>)
            )
        }
    }
}