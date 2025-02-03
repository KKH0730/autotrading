package st.seno.autotrading.data.network

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import okhttp3.Interceptor
import okhttp3.Response
import st.seno.autotrading.data.network.model.Asset
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Base64
import java.util.UUID
import javax.inject.Inject


/**
 * Retrofit을 이용한 REST API에 공통 헤더를 추가하는 interceptor
 */

// Todo: key들은 local properties로 이전 필요
const val SECRET_KEY = "FjkDfW3K6uSd1T4yQpgvx1cjJY5xlOb2MGCWNslC"
const val ACCESS_KEY = "HAQHEpfACO4BFhUowJfPUkaRqXZA3nYSKKCoVQoC"

class DefaultParamsInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        val originalRequest = chain.request()
        val url = originalRequest.url
        val queryElements = ArrayList<String>()

        url.queryParameterNames.forEach { key ->
            queryElements.add((key + "=" + url.queryParameter(key)))
        }
        // 정렬된 쿼리 문자열 생성
        val queryString = java.lang.String.join("&", *queryElements.toTypedArray<String>())

        val md = MessageDigest.getInstance("SHA-512")
        md.update(queryString.toByteArray(charset("UTF-8")))

        // SHA-512 해싱 후 Base64 인코딩
        val queryHash = String.format("%0128x", BigInteger(1, md.digest()))

        val algorithm: Algorithm = Algorithm.HMAC256(SECRET_KEY)
        val jwtToken: String = JWT.create()
            .withClaim("access_key", ACCESS_KEY)
            .withClaim("nonce", UUID.randomUUID().toString())
            .withClaim("query_hash", queryHash)
            .withClaim("query_hash_alg", "SHA512")
            .sign(algorithm)
        val authenticationToken = "Bearer $jwtToken"

        getDefaultHeader(authenticationToken = authenticationToken).forEach { (key, value) ->
            builder.addHeader(key, value)
        }

        return chain.proceed(builder.build())
    }

    /**
     * Rest API 에서 사용하는 default headers
     */
    private fun getDefaultHeader(authenticationToken: String): Map<String, String> {
//        val algorithm: Algorithm = Algorithm.HMAC256(SECRET_KEY)
//        val jwtToken: String = JWT.create()
//            .withClaim("access_key", ACCESS_KEY)
//            .withClaim("nonce", UUID.randomUUID().toString())
//            .sign(algorithm)
//        val authenticationToken = "Bearer $jwtToken"

        return mutableMapOf(
            "Content-Type" to "application/json",
            "Authorization" to authenticationToken
        ).apply {

        }
    }
}