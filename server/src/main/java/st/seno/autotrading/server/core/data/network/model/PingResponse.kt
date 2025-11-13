package st.seno.autotrading.server.core.data.network.model

import com.google.gson.annotations.SerializedName

data class PingResponse(
    @SerializedName("status")
    val status: String
)