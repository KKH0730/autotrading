package st.seno.autotrading.server.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "upbit")
data class UpbitProperties(
    var accessKey: String = "",
    var secretKey: String = ""
)