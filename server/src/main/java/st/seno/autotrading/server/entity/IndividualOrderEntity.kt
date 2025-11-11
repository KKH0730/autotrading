package st.seno.autotrading.server.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(name = "individual_order")
data class IndividualOrderEntity(
    @Id
    @Column(name = "uuid", nullable = false)
    val uuid: String, // 주문 고유 ID
    @Column(name = "user_key", nullable = false)
    val userKey: String,
    @Column(name = "side", nullable = false)
    val side: String,
    @Column(name = "ord_type", nullable = false)
    val ordType: String,
    @Column(name = "price")
    val price: BigDecimal?,
    @Column(name = "state", nullable = false)
    val state: String,
    @Column(name = "market", nullable = false)
    val market: String,
    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime,
    @Column(name = "volume")
    val volume: BigDecimal?,
    @Column(name = "remaining_volume")
    val remainingVolume: BigDecimal?,
    @Column(name = "reserved_fee", nullable = false)
    val reservedFee: BigDecimal,
    @Column(name = "remaining_fee", nullable = false)
    val remainingFee: BigDecimal,
    @Column(name = "paid_fee", nullable = false)
    val paidFee: BigDecimal,
    @Column(name = "locked", nullable = false)
    val locked: BigDecimal,
    @Column(name = "executed_volume", nullable = false)
    val executedVolume: BigDecimal,
    @Column(name = "trades_count", nullable = false)
    val tradesCount: Int,
    @Column(name = "time_in_force")
    val timeInForce: String?,
    @Column(name = "identifier")
    val identifier: String?,
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    var trades: MutableList<TradeEntity> = mutableListOf()
) {
    constructor() : this(
        uuid = "",
        userKey = "",
        side = "",
        ordType = "",
        price = null,
        state = "",
        market = "",
        createdAt = OffsetDateTime.now(),
        volume = null,
        remainingVolume = null,
        reservedFee = BigDecimal.ZERO,
        remainingFee = BigDecimal.ZERO,
        paidFee = BigDecimal.ZERO,
        locked = BigDecimal.ZERO,
        executedVolume = BigDecimal.ZERO,
        tradesCount = 0,
        timeInForce = null,
        identifier = null,
        trades = mutableListOf()
    )
}


@Entity
@Table(name = "trade")
data class TradeEntity(
    @Id
    @Column(name = "trades_uuid", nullable = false)
    val tradesUuid: String, // 체결 고유 ID
    @Column(name = "trades_market", nullable = false)
    val tradesMarket: String,
    @Column(name = "trades_price", nullable = false)
    val tradesPrice: BigDecimal,
    @Column(name = "trades_volume", nullable = false)
    val tradesVolume: BigDecimal,
    @Column(name = "trades_funds", nullable = false)
    val tradesFunds: BigDecimal,
    @Column(name = "trades_side", nullable = false)
    val tradesSide: String,
    @Column(name = "trades_created_at", nullable = false)
    var tradesCreatedAt: String,
    @Column(name = "user_key", nullable = false)
    val userKey: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_uuid")
    var order: IndividualOrderEntity? = null
) {
    constructor() : this(
        tradesUuid = "",
        tradesMarket = "",
        tradesPrice = BigDecimal.ZERO,
        tradesVolume = BigDecimal.ZERO,
        tradesFunds = BigDecimal.ZERO,
        tradesSide = "",
        tradesCreatedAt = "",
        userKey = "",
        order = null
    )
}