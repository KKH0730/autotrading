package st.seno.autotrading.server.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "counts")
class CountEntity @JvmOverloads constructor(
    @Id
    var id: Int? = null,
    var countValue: Int = 0
)