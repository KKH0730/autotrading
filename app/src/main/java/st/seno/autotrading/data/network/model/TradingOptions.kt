package st.seno.autotrading.data.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TradingOptions(
    @SerializedName("market_id")
    val marketId: String?,
    @SerializedName("quantity_ratio")
    val quantityRatio: Int?,
    @SerializedName("stop_loss")
    val stopLoss: Int?,
    @SerializedName("stop_loss_price")
    val stopLossPrice: String?,
    @SerializedName("take_profit")
    val takeProfit: Int?,
    @SerializedName("take_profit_price")
    val takeProfitPrice: String?,
    @SerializedName("correction_value")
    val correctionValue: Float?,
    @SerializedName("start_date")
    val startDate: Long?,
    @SerializedName("end_date")
    val endDateTime: Long?,
    @SerializedName("trading_strategy")
    val tradingStrategy: String?
): Parcelable