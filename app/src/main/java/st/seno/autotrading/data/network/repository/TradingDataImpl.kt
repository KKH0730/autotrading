package st.seno.autotrading.data.network.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.seno.autotrading.data.mapper.TradingDataMapper
import st.seno.autotrading.data.network.response_model.TradingData
import st.seno.autotrading.keyname.KeyName
import javax.inject.Inject

class TradingDataImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val mapper: TradingDataMapper
) : TradingDataRepository {

    override suspend fun reqTradingData(startDate: String): Flow<List<TradingData>> = callbackFlow {
        val listenerRegistration: ListenerRegistration =
            firestore.collection(KeyName.Firestore.TRADING_COLLECTION)
                .document(KeyName.Firestore.AUTO_TRADING_DOCUMENT)
                .collection(startDate)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        close(error) // Flow 종료
                        return@addSnapshotListener
                    }

                    val data = value?.map { mapper.fromRemote(model = it) } ?: emptyList()
                    trySend(data)
                }

        awaitClose { listenerRegistration.remove() }
    }
}