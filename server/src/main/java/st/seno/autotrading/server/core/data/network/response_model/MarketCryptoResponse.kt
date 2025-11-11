package st.seno.autotrading.server.core.data.network.response_model

import com.fasterxml.jackson.annotation.JsonProperty

data class MarketCryptoResponse(
    @JsonProperty("market")
    val market: String,
    @JsonProperty("korean_name")
    val koreanName: String,
    @JsonProperty("english_name")
    val englishName: String
)