package st.seno.autotrading.server.core.data.network.repository

import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient
import st.seno.autotrading.server.core.data.network.model.Candle
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Repository
import st.seno.autotrading.server.core.data.mapper.DaysCandleMapper
import st.seno.autotrading.server.core.data.mapper.MinutesCandleMapper
import st.seno.autotrading.server.core.data.mapper.MonthsCandleMapper
import st.seno.autotrading.server.core.data.mapper.SecondsCandleMapper
import st.seno.autotrading.server.core.data.mapper.WeeksCandleMapper
import st.seno.autotrading.server.core.data.mapper.YearsCandleMapper
import st.seno.autotrading.server.core.data.network.response_model.DaysCandleResponse
import st.seno.autotrading.server.core.data.network.response_model.MinutesCandleResponse
import st.seno.autotrading.server.core.data.network.response_model.MonthsCandleResponse
import st.seno.autotrading.server.core.data.network.response_model.SecondsCandleResponse
import st.seno.autotrading.server.core.data.network.response_model.WeeksCandleResponse
import st.seno.autotrading.server.core.data.network.response_model.YearsCandleResponse

@Repository
class CandleImpl(
    private val webClient: WebClient,
    private val yearsCandleMapper: YearsCandleMapper,
    private val monthsCandleMapper: MonthsCandleMapper,
    private val weeksCandleMapper: WeeksCandleMapper,
    private val daysCandleMapper: DaysCandleMapper,
    private val minutesCandleMapper: MinutesCandleMapper,
    private val secondsCandleMapper: SecondsCandleMapper,
) : CandleRepository {
    override suspend fun reqYearsCandle(
        market: String,
        to: String,
        count: Int
    ): List<Candle> {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/candles/years")
                    .queryParam("market", market)
                    .queryParam("to", to)
                    .queryParam("count", count)
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<YearsCandleResponse>>() {})
            .awaitSingle()

        return response.map { yearsCandleMapper.fromRemote(it) }
    }

    override suspend fun reqMonthsCandle(
        market: String,
        to: String,
        count: Int
    ): List<Candle> {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/candles/months")
                    .queryParam("market", market)
                    .queryParam("to", to)
                    .queryParam("count", count)
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<MonthsCandleResponse>>() {})
            .awaitSingle()

        return response.map { monthsCandleMapper.fromRemote(it) }
    }

    override suspend fun reqWeeksCandle(
        market: String,
        to: String,
        count: Int
    ): List<Candle> {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/candles/weeks")
                    .queryParam("market", market)
                    .queryParam("to", to)
                    .queryParam("count", count)
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<WeeksCandleResponse>>() {})
            .awaitSingle()

        return response.map { weeksCandleMapper.fromRemote(it) }
    }

    override suspend fun reqDaysCandle(
        market: String,
        to: String?,
        count: Int,
    ): List<Candle> {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/candles/days")
                    .queryParam("market", market)
                    .queryParam("to", to)
                    .queryParam("count", count)
                    .queryParam("converting_price_unit", null)
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<DaysCandleResponse>>() {})
            .awaitSingle()

        return response.map { daysCandleMapper.fromRemote(it) }
    }

    override suspend fun reqMinutesCandle(
        market: String,
        to: String,
        count: Int,
        unit: Int?
    ): List<Candle> {
        val uri = "/candles/minutes/${unit}"

        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path(uri)
                    .queryParam("market", market)
                    .queryParam("to", to)
                    .queryParam("count", count)
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<MinutesCandleResponse>>() {})
            .awaitSingle()

        return response.map { minutesCandleMapper.fromRemote(it) }
    }

    override suspend fun reqSecondsCandle(
        market: String,
        to: String,
        count: Int
    ): List<Candle> {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/candles/seconds")
                    .queryParam("market", market)
                    .queryParam("to", to)
                    .queryParam("count", count)
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<SecondsCandleResponse>>() {})
            .awaitSingle()

        return response.map { secondsCandleMapper.fromRemote(it) }
    }
}