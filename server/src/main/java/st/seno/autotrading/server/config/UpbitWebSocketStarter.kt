package st.seno.autotrading.server.config

import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import st.seno.autotrading.server.core.data.network.model.Ticker
import st.seno.autotrading.server.core.data.network.model.isSuccess
import st.seno.autotrading.server.core.data.network.model.successData
import st.seno.autotrading.server.service.websocket.UpbitWebSocketClient
import st.seno.autotrading.server.core.domain.MarketUseCase

@Component
class UpbitWebSocketStarter(
    private val upbitWebSocketClient: UpbitWebSocketClient,
    private val marketUseCase: MarketUseCase
) {
    companion object {
        val tickersMap: MutableMap<String, Ticker> = mutableMapOf()
    }

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady() = runBlocking {
        println("✅ 서버 시작됨 — 업비트 웹소켓 연결 시작")

        val result = marketUseCase.reqMarketCrypto()
        if (result.isSuccess()) {
            val marketIdList = result.successData().map { it.marketId }
            upbitWebSocketClient.connect(marketIdList)
        }
    }
}
