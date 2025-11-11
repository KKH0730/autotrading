package st.seno.autotrading.server.repository

import st.seno.autotrading.server.entity.CountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CountRepository : JpaRepository<CountEntity, Int>