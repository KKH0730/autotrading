package st.seno.autotrading.server.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "auto_trading_service")
data class AutoTradingServiceEntity(
    @Id
    @Column(name = "user_key")
    val userKey: String = "",
    @Column(name = "is_auto_trading_running")
    val isAutoTradingRunning: Boolean = false,
    @Column(name = "market_id")
    val marketId: String? = null,
    @Column(name = "quantity_ratio")
    val quantityRatio: Int? = null,
    @Column(name = "stop_loss")
    val stopLoss: Int? = null,
    @Column(name = "stop_loss_price")
    var stopLossPrice: String ?= "매수체결 후에 결정됩니다.",
    @Column(name = "take_profit")
    val takeProfit: Int? = null,
    @Column(name = "take_profit_price")
    var takeProfitPrice: String? = "매수체결 후에 결정됩니다.",
    @Column(name = "correction_value")
    val correctionValue: Float? = null,
    @Column(name = "start_date")
    val startDate: Long? = null,
    @Column(name = "end_date")
    val endDate: Long? = null,
    @Column(name = "trading_strategy")
    val tradingStrategy: String? = null
)