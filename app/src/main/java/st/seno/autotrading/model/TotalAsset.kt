package st.seno.autotrading.model

import st.seno.autotrading.data.network.model.Asset

data class TotalAsset(
    val totalEvaluatedPrice: Double = 0.0, // 평가금액
    val totalSignedChangeRate: Double = 0.0, // 수익률
    val assets: List<Asset> = listOf()
)