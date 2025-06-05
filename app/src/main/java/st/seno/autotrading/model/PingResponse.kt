package st.seno.autotrading.model

import com.google.gson.annotations.SerializedName

data class PingResponse(
    @SerializedName("status")
    val status: String
)