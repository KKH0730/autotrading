package st.seno.autotrading.server.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import st.seno.autotrading.server.entity.AutoTradingHistoryEntity
import st.seno.autotrading.server.entity.AutoTradingHistoryPeriodEntity
import st.seno.autotrading.server.entity.AutoTradingLogEntity
import st.seno.autotrading.server.entity.AutoTradingServiceEntity
import st.seno.autotrading.server.entity.IndividualOrderEntity
import st.seno.autotrading.server.entity.TradeEntity

interface AutoTradingServiceRepository : JpaRepository<AutoTradingServiceEntity, String> {
    @Query("SELECT p FROM AutoTradingServiceEntity p WHERE p.userKey = :userKey")
    fun findByUserKey(@Param("userKey") userKey: String): AutoTradingServiceEntity

    @Modifying
    @Transactional
    @Query("""
    UPDATE AutoTradingServiceEntity p
    SET p.stopLossPrice = :stopLossPrice,
        p.takeProfitPrice = :takeProfitPrice
    WHERE p.userKey = :userKey
""")
    fun updatePrices(
        @Param("userKey") userKey: String,
        @Param("stopLossPrice") stopLossPrice: String,
        @Param("takeProfitPrice") takeProfitPrice: String
    )
}

interface AutoTradingLogRepository : JpaRepository<AutoTradingLogEntity, Long>

interface AutoTradingHistoryPeriodRepository : JpaRepository<AutoTradingHistoryPeriodEntity, Long> {
    @Query("SELECT p FROM AutoTradingHistoryPeriodEntity p LEFT JOIN FETCH p.histories WHERE p.userKey = :userKey AND p.startDate = :startDate")
    fun findByStartDateWithHistories(
        @Param("userKey") userKey: String,
        @Param("startDate") startDate: String
    ): AutoTradingHistoryPeriodEntity?
}

interface AutoTradingHistoryRepository : JpaRepository<AutoTradingHistoryEntity, Long> {
    @Query("SELECT p FROM AutoTradingHistoryEntity p WHERE p.userKey = :userKey AND p.startDate = :startDate")
    fun findAllByStartDate(
        @Param("userKey") userKey: String,
        @Param("startDate") startDate: String
    ): List<AutoTradingHistoryEntity>
}

interface IndividualOrderRepository : JpaRepository<IndividualOrderEntity, String> {
    @Query("SELECT p FROM IndividualOrderEntity p WHERE p.userKey = :userKey AND p.uuid = :bidUuid")
    fun findByBidUuid(
        @Param("userKey") userKey: String,
        @Param("bidUuid") bidUuid: String
    ): IndividualOrderEntity?
}

interface TradeRepository : JpaRepository<TradeEntity, String> {
    @Query("SELECT p FROM TradeEntity p WHERE p.userKey = :userKey AND p.order.uuid = :orderUuid")
    fun findByOrderUuid(
        @Param("userKey") userKey: String,
        @Param("orderUuid") orderUuid: String
    ): List<TradeEntity>
}