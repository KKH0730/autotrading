package st.seno.autotrading.server.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "auto_trading_history_period")
data class AutoTradingHistoryPeriodEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "user_key", nullable = false)
    val userKey: String,
    @Column(name = "start_date", nullable = false)
    val startDate: String,
    @Column(name = "end_date", nullable = false)
    val endDate: String,
    @OneToMany(
        mappedBy = "period",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    val histories: MutableList<AutoTradingHistoryEntity> = mutableListOf()
) {
    constructor() : this(userKey = "", startDate = "", endDate = "")
}

@Entity
@Table(name = "auto_trading_history")
data class AutoTradingHistoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,
    @Column(name = "uuid", nullable = false)
    val uuid: String, // 수동 UUID 할당
    @Column(name = "bid_uuid", nullable = false)
    val bidUuid: String,
    @Column(name = "individual_uuid", nullable = false)
    val individualUuid: String,
    @Column(name = "user_key", nullable = false)
    val userKey: String,
    @Column(name = "side", nullable = false)
    val side: String,
    @Column(name = "quantity_ratio", nullable = false)
    val quantityRatio: Int,
    @Column(name = "trading_strategy", nullable = false)
    val tradingStrategy: String,
    @Column(name = "stop_loss", nullable = false)
    val stopLoss: Int,
    @Column(name = "take_profit", nullable = false)
    val takeProfit: Int,
    @Column(name = "correction_value", nullable = false)
    val correctionValue: Float,
    @Column(name = "start_date", nullable = false)
    val startDate: String,
    @Column(name = "end_date", nullable = false)
    val endDate: String,
    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    val period: AutoTradingHistoryPeriodEntity
) {
    constructor() : this(
        uuid = "",
        bidUuid = "",
        individualUuid = "",
        userKey = "",
        side = "",
        quantityRatio = 0,
        tradingStrategy = "",
        stopLoss = 0,
        takeProfit = 0,
        correctionValue = 0f,
        startDate = "",
        endDate = "",
        createdAt = OffsetDateTime.now(),
        period = AutoTradingHistoryPeriodEntity()
    )
}