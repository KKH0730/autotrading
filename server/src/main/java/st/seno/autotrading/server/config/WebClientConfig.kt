package st.seno.autotrading.server.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.security.MessageDigest
import java.util.UUID

@Configuration
class WebClientConfig(
    private val upbitProperties: UpbitProperties
){

    @Bean
    fun upbitWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://api.upbit.com/v1")
            .filter(authFilter())
            .defaultHeader("Content-Type", "application/json")
            .filter(logRequest())
            .filter(logResponse())
            .build()
    }

    private fun authFilter(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { request ->
            val method = request.method()
            val uri = request.url()
            var queryString = uri.query ?: ""

            // POST/PUT/PATCH body에서 queryString 생성
            val bodyString = request.attribute("bodyString").orElse(null) as? String
            if (!bodyString.isNullOrBlank()) {
                queryString = jsonToQueryString(bodyString)
            }

            println("method=$method, queryString=$queryString")

            // SHA-512 해시
            val md = MessageDigest.getInstance("SHA-512")
            md.update(queryString.toByteArray(Charsets.UTF_8))
            val queryHash = md.digest().joinToString("") { "%02x".format(it) }

            // JWT 생성 (HMAC512)
            val algorithm = Algorithm.HMAC512(upbitProperties.secretKey.toByteArray(Charsets.UTF_8))
            val jwtBuilder = JWT.create()
                .withClaim("access_key", upbitProperties.accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())

            if (queryHash.isNotBlank()) {
                jwtBuilder.withClaim("query_hash", queryHash)
                jwtBuilder.withClaim("query_hash_alg", "SHA512")
            }

            val jwtToken = jwtBuilder.sign(algorithm)
            val bearer = "Bearer $jwtToken"

            val newRequest =  ClientRequest.from(request)
                .header("Authorization", bearer)
                .build()

            Mono.just(newRequest)
        }
    }

    /**
     * JSON → query string 변환
     * 순서 유지 + null/빈 값 제거
     */
    private fun jsonToQueryString(jsonString: String): String {
        val regex = "\"(.*?)\"\\s*:\\s*(\".*?\"|null|\\d+\\.?\\d*)".toRegex()
        return regex.findAll(jsonString)
            .mapNotNull { match ->
                val key = match.groupValues[1]
                val rawValue = match.groupValues[2].trim('"')
                if (rawValue.isNotBlank() && rawValue.lowercase() != "null") {
                    "$key=$rawValue"
                } else null
            }
            .joinToString("&")
    }

    private fun logRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { request ->
            println("🛰 [WebClient Request]")
            println("➡️ Method: ${request.method()}")
            println("➡️ URL: ${request.url()}")
            println("➡️ Headers: ${request.headers()}")
            Mono.just(request)
        }
    }

    private fun logResponse(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor { response ->
            println("🛰 [WebClient Response]")
            println("⬅️ Status: ${response.statusCode()}")
            response.headers().asHttpHeaders().forEach { (k, v) ->
                println("⬅️ Header: $k -> $v")
            }
            Mono.just(response)
        }
    }
}
