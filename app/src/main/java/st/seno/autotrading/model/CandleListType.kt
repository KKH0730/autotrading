package st.seno.autotrading.model

import st.seno.autotrading.data.network.model.Candle

sealed class CandleListType {
    data class CandleType(val candle: Candle): CandleListType()
    data class Blank(val width: Int = 50): CandleListType()
}