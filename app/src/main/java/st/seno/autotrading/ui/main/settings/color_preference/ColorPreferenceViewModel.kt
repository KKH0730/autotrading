package st.seno.autotrading.ui.main.settings.color_preference

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ColorPreferenceViewModel @Inject constructor() : BaseViewModel() {
    private val _candles: MutableStateFlow<List<Candle>> = MutableStateFlow(listOf())
    val candles: StateFlow<List<Candle>> get() = _candles.asStateFlow()

    init {
        makeSampleCandleList()
    }

    private fun makeSampleCandleList() {
        _candles.value = listOf(
            Candle(openingPrice = 162.0, highPrice = 162.0, lowPrice = 120.0, tradePrice = 129.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 163.0, highPrice = 167.0, lowPrice = 162.0, tradePrice = 162.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 159.0, highPrice = 164.0, lowPrice = 156.0, tradePrice = 163.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 157.0, highPrice = 162.0, lowPrice = 155.0, tradePrice = 159.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 156.0, highPrice = 160.0, lowPrice = 155.0, tradePrice = 157.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 155.0, highPrice = 160.0, lowPrice = 152.0, tradePrice = 156.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 148.0, highPrice = 155.0, lowPrice = 148.0, tradePrice = 155.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 149.0, highPrice = 150.0, lowPrice = 148.0, tradePrice = 148.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 151.0, highPrice = 154.0, lowPrice = 149.0, tradePrice = 149.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 152.0, highPrice = 155.0, lowPrice = 151.0, tradePrice = 151.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 156.0, highPrice = 158.0, lowPrice = 150.0, tradePrice = 152.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 157.0, highPrice = 158.0, lowPrice = 155.0, tradePrice = 156.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 168.0, highPrice = 168.0, lowPrice = 155.0, tradePrice = 157.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 167.0, highPrice = 170.0, lowPrice = 167.0, tradePrice = 168.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 146.0, highPrice = 167.0, lowPrice = 146.0, tradePrice = 167.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 149.0, highPrice = 150.0, lowPrice = 146.0, tradePrice = 146.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 153.0, highPrice = 155.0, lowPrice = 149.0, tradePrice = 149.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 155.0, highPrice = 160.0, lowPrice = 152.0, tradePrice = 153.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 166.0, highPrice = 166.0, lowPrice = 150.0, tradePrice = 155.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 150.0, highPrice = 167.0, lowPrice = 149.0, tradePrice = 166.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 152.0, highPrice = 155.0, lowPrice = 149.0, tradePrice = 150.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 158.0, highPrice = 158.0, lowPrice = 152.0, tradePrice = 152.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 155.0, highPrice = 160.0, lowPrice = 154.0, tradePrice = 158.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 154.0, highPrice = 157.0, lowPrice = 152.0, tradePrice = 155.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 155.0, highPrice = 157.0, lowPrice = 153.0, tradePrice = 154.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 143.0, highPrice = 155.0, lowPrice = 143.0, tradePrice = 155.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 140.0, highPrice = 143.0, lowPrice = 139.0, tradePrice = 143.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 131.0, highPrice = 140.0, lowPrice = 131.0, tradePrice = 140.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 130.0, highPrice = 150.0, lowPrice = 129.0, tradePrice = 131.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 133.0, highPrice = 135.0, lowPrice = 129.0, tradePrice = 130.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 125.0, highPrice = 130.0, lowPrice = 121.0, tradePrice = 133.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 124.0, highPrice = 128.0, lowPrice = 121.0, tradePrice = 125.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 124.0, highPrice = 128.0, lowPrice = 121.0, tradePrice = 125.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 126.0, highPrice = 130.0, lowPrice = 120.0, tradePrice = 124.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 127.0, highPrice = 129.0, lowPrice = 125.0, tradePrice = 126.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 128.0, highPrice = 129.0, lowPrice = 126.0, tradePrice = 127.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 136.0, highPrice = 137.0, lowPrice = 127.0, tradePrice = 128.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 144.0, highPrice = 146.0, lowPrice = 132.0, tradePrice = 136.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 150.0, highPrice = 155.0, lowPrice = 144.0, tradePrice = 144.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 157.0, highPrice = 160.0, lowPrice = 150.0, tradePrice = 150.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 170.0, highPrice = 171.0, lowPrice = 155.0, tradePrice = 157.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 150.0, highPrice = 170.0, lowPrice = 149.0, tradePrice = 170.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 145.0, highPrice = 152.0, lowPrice = 134.0, tradePrice = 150.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 136.0, highPrice = 147.0, lowPrice = 134.0, tradePrice = 145.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 135.0, highPrice = 140.0, lowPrice = 134.0, tradePrice = 136.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 127.0, highPrice = 138.0, lowPrice = 120.0, tradePrice = 135.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 110.0, highPrice = 128.0, lowPrice = 110.0, tradePrice = 127.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 116.0, highPrice = 124.0, lowPrice = 109.0, tradePrice = 110.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 122.0, highPrice = 125.0, lowPrice = 115.0, tradePrice = 116.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 120.0, highPrice = 125.0, lowPrice = 118.0, tradePrice = 122.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 115.0, highPrice = 120.0, lowPrice = 110.0, tradePrice = 120.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 122.0, highPrice = 125.0, lowPrice = 115.0, tradePrice = 115.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 141.0, highPrice = 141.0, lowPrice = 120.0, tradePrice = 122.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 135.0, highPrice = 142.0, lowPrice = 133.0, tradePrice = 141.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 135.0, highPrice = 142.0, lowPrice = 133.0, tradePrice = 141.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 145.0, highPrice = 148.0, lowPrice = 132.0, tradePrice = 135.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 125.0, highPrice = 150.0, lowPrice = 120.0, tradePrice = 145.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 106.0, highPrice = 130.0, lowPrice = 100.0, tradePrice = 125.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 110.0, highPrice = 115.0, lowPrice = 105.0, tradePrice = 106.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
            Candle(openingPrice = 120.0, highPrice = 130.0, lowPrice = 115.0, tradePrice = 110.0, timestamp = 0L, candleAccTradePrice = 0.0, candleAccTradeVolume = 0.0,),
        )
    }
}