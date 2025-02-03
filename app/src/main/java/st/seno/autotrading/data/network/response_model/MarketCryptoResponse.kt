package st.seno.autotrading.data.network.response_model

import com.google.gson.annotations.SerializedName

data class MarketCryptoResponse(
    @SerializedName("market")
    val market: String,
    @SerializedName("korean_name")
    val koreanName: String,
    @SerializedName("english_name")
    val englishName: String
)