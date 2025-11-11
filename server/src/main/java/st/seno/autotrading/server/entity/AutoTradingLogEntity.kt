package st.seno.autotrading.server.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "auto_trading_log")
data class AutoTradingLogEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(columnDefinition = "TEXT", nullable = false)
    val log: String,
    @Column(name = "created_at", updatable = false, insertable = false)
    val createdAt: LocalDateTime? = null
)