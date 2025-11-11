package st.seno.autotrading.server.service.rest_api


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import st.seno.autotrading.server.core.extension.formatRealPrice
import st.seno.autotrading.server.config.UpbitWebSocketStarter
import st.seno.autotrading.server.core.data.network.model.isSuccess
import st.seno.autotrading.server.core.data.network.model.successData
import st.seno.autotrading.server.core.data.network.model.Asset
import st.seno.autotrading.server.core.domain.CandleUseCase
import st.seno.autotrading.server.core.extension.stringToLocalDateTime
import st.seno.autotrading.server.core.data.network.model.Candle
import st.seno.autotrading.server.core.data.network.model.IndividualOrder
import st.seno.autotrading.server.core.data.network.model.Order
import st.seno.autotrading.server.core.data.network.model.Trade
import st.seno.autotrading.server.core.data.network.model.TradingData
import st.seno.autotrading.server.core.domain.MyAssetsUseCase
import st.seno.autotrading.server.core.domain.OrderUseCase
import st.seno.autotrading.server.core.extension.parseDateFormat
import st.seno.autotrading.server.core.extension.toLocalDateTime
import st.seno.autotrading.server.core.extension.toSeoulTime
import st.seno.autotrading.server.entity.AutoTradingHistoryEntity
import st.seno.autotrading.server.entity.AutoTradingHistoryPeriodEntity
import st.seno.autotrading.server.entity.AutoTradingLogEntity
import st.seno.autotrading.server.entity.AutoTradingServiceEntity
import st.seno.autotrading.server.entity.IndividualOrderEntity
import st.seno.autotrading.server.entity.TradeEntity
import st.seno.autotrading.server.model.AutoTradingConstants
import st.seno.autotrading.server.model.OrderType
import st.seno.autotrading.server.model.Side
import st.seno.autotrading.server.repository.AutoTradingHistoryPeriodRepository
import st.seno.autotrading.server.repository.AutoTradingHistoryRepository
import st.seno.autotrading.server.repository.AutoTradingLogRepository
import st.seno.autotrading.server.repository.AutoTradingServiceRepository
import st.seno.autotrading.server.repository.IndividualOrderRepository
import st.seno.autotrading.server.service.websocket.AutoTradingSocketHandler
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class AutoTradingApiService(
    private val logRepository: AutoTradingLogRepository,
    private val autoTradingServiceRepository: AutoTradingServiceRepository,
    private val historyPeriodRepository: AutoTradingHistoryPeriodRepository,
    private val historyRepository: AutoTradingHistoryRepository,
    private val individualOrderRepository: IndividualOrderRepository,
    private val candleUseCase: CandleUseCase,
    private val myAssetsUseCase: MyAssetsUseCase,
    private val orderUseCase: OrderUseCase,
    private val autoTradingHandler: AutoTradingSocketHandler
) {
    var tradingJobMap: MutableMap<String, Job> = mutableMapOf()
    private val fee: Double = 0.0005 // 업비트 수수료

    @Transactional
    fun isRunningAutoTrading(userKey: String): AutoTradingServiceEntity {
        var entity = autoTradingServiceRepository.findByUserKey(userKey = userKey)

        val job: Job? = tradingJobMap[userKey]
        if (entity.isAutoTradingRunning && (job == null || !job.isActive)) {
            entity = stopAutoTrading(userKey = userKey)
        } else {
            autoTradingHandler.sendMessageToClient(
                userKey = userKey,
                map = mapOf(
                    "mode" to AutoTradingConstants.OPERATION,
                    "data" to mapOf(
                        "isRunning" to entity.isAutoTradingRunning,
                        "message" to if(entity.isAutoTradingRunning) "Service is running" else "Service is stopped",
                        "tradingOptions" to mapOf(
                            "user_key" to entity.userKey,
                            "market_id" to entity.marketId,
                            "quantity_ratio" to entity.quantityRatio,
                            "stop_loss" to entity.stopLoss,
                            "stop_loss_price" to entity.stopLossPrice,
                            "take_profit" to entity.takeProfit,
                            "take_profit_price" to entity.takeProfitPrice,
                            "correction_value" to entity.correctionValue,
                            "start_date" to entity.startDate,
                            "end_date" to entity.endDate,
                            "trading_strategy" to entity.tradingStrategy
                        )
                    )
                )
            )
        }
        return entity
    }

    @Transactional
    fun stopAutoTrading(userKey: String): AutoTradingServiceEntity {
        val job = tradingJobMap[userKey]
        job?.cancel()
        val entity = autoTradingServiceRepository.save(
            AutoTradingServiceEntity(
                userKey = userKey,
                isAutoTradingRunning = false,
                marketId = null,
                quantityRatio = null,
                stopLoss = null,
                takeProfit = null,
                correctionValue = null,
                startDate = null,
                endDate = null,
                tradingStrategy = null
            )
        )

        autoTradingHandler.sendMessageToClient(
            userKey = userKey,
            map = mapOf(
                "mode" to AutoTradingConstants.OPERATION,
                "data" to mapOf(
                    "mode" to AutoTradingConstants.OPERATION,
                    "isRunning" to entity.isAutoTradingRunning,
                    "message" to "Service is stopped"
                )
            )
        )

        return entity
    }

    @Transactional
    fun startTrading(
        marketId: String,
        quantityRatio: Int,
        stopLoss: Int,
        takeProfit: Int,
        correctionValue: Float,
        startDateTime: Long,
        endDateTime: Long,
        tradingStrategy: String,
        userKey: String
    ): AutoTradingServiceEntity {
        val autoTradingServiceEntity = autoTradingServiceRepository.save(
            AutoTradingServiceEntity(
                userKey = userKey,
                isAutoTradingRunning = true,
                marketId = marketId,
                quantityRatio = quantityRatio,
                stopLoss = stopLoss,
                takeProfit = takeProfit,
                correctionValue = correctionValue,
                startDate = startDateTime,
                endDate = endDateTime,
                tradingStrategy = tradingStrategy
            )
        )

        val startDate = startDateTime.toLocalDateTime()
        val endDate = endDateTime.toLocalDateTime()
        var bidOrder: Order? = null
        var bidPrice: Double = 0.0
        var dayCandles: List<Candle> = listOf()
        var isSkipBid = false

        tradingJobMap[userKey] = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                println("now : ${now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}, endDate : ${endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}")
                if (now.isAfter(endDate) && bidOrder == null) {
                    println("end")
                    saveLog(log = "trading end => now : ${now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}, endDate : ${endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}")
                    stopAutoTrading(userKey = userKey)
                    tradingJobMap[userKey]?.cancel()
                    return@launch
                }

                dayCandles = if (dayCandles.isEmpty() || dayCandles.firstOrNull()?.let { isRequestNewCandle(now, it) } == true) {
                    getDayCandles(marketId)
                } else {
                    dayCandles
                }

                // 매수 가능 체크
                if (isBidTime(now = now) && isCanBid(order = bidOrder) && !isSkipBid) {
                    val (order, tradePrice) = executeBuy(
                        userKey = userKey,
                        marketId = marketId,
                        quantityRatio = quantityRatio,
                        stopLoss = stopLoss,
                        takeProfit = takeProfit,
                        correctionValue = correctionValue,
                        dayCandles = dayCandles,
                        startDate = startDate,
                        endDate = endDate,
                        tradingStrategy = tradingStrategy,
                        onSkipBid = { isSkipBid = true },
                    ) ?: Pair(null, null)

                    bidOrder = order
                    bidPrice = tradePrice ?: 0.0

                    if (bidPrice != 0.0) {
                        autoTradingServiceRepository.updatePrices(
                            userKey = userKey,
                            stopLossPrice = (bidPrice * ((100 - stopLoss) / 100.0)).formatRealPrice(),
                            takeProfitPrice = (bidPrice * (1 + (takeProfit / 100.0))).formatRealPrice()
                        )

                        getTradingHistory(userKey = userKey)
                    }

                    println("bid : $bidOrder")
                }

                // StopLoss 혹은 TakeProfit 체크
                if (isCanAsk(order = bidOrder) && bidPrice != 0.0) {
                    executeSell(
                        userKey = userKey,
                        marketId = marketId,
                        quantityRatio = quantityRatio,
                        stopLoss = stopLoss,
                        takeProfit = takeProfit,
                        correctionValue = correctionValue,
                        bidUuid = bidOrder?.uuid ?: "",
                        bidPrice = bidPrice,
                        now = now,
                        startDate = startDate,
                        endDate = endDate,
                        tradingStrategy = tradingStrategy
                    )?.also {
                        bidOrder = null
                        isSkipBid = true
                        bidPrice = 0.0
                        dayCandles = listOf()
                    }
                }

                // 매도 가능 체크
//                if (isAskTime(now = now)) {
//                    if (isCanAsk(order = bidOrder)) {
                if (true) {
                    if (true) {
                        sellCrypto(marketId = marketId)?.let { askOrder ->
                            println("ask : $askOrder")
                            saveAutoTradingHistory(
                                userKey = userKey,
                                uuid = askOrder.uuid,
                                bidUuid = bidOrder?.uuid ?: "",
                                side = Side.ASK.value,
                                quantityRatio = quantityRatio,
                                tradingStrategy = tradingStrategy,
                                stopLoss = stopLoss,
                                takeProfit = takeProfit,
                                correctionValue = correctionValue,
                                startDate = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                endDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                individualOrder = reqIndividualOrder(uuid = askOrder.uuid)
                            )

                            bidOrder = null
                            bidPrice = 0.0
                            dayCandles = listOf()

                            getTradingHistory(userKey = userKey)
                        }
                    }
                    isSkipBid = false
                }

                delay(timeMillis = 10000)
            }
        }

        autoTradingHandler.sendMessageToClient(
            userKey = userKey,
            map = mapOf(
                "mode" to AutoTradingConstants.OPERATION,
                "data" to mapOf(
                    "isRunning" to autoTradingServiceEntity.isAutoTradingRunning,
                    "message" to "Service is running",
                    "tradingOptions" to mapOf(
                        "user_key" to autoTradingServiceEntity.userKey,
                        "market_id" to autoTradingServiceEntity.marketId,
                        "quantity_ratio" to autoTradingServiceEntity.quantityRatio,
                        "stop_loss" to autoTradingServiceEntity.stopLoss,
                        "stop_loss_price" to autoTradingServiceEntity.stopLossPrice,
                        "take_profit" to autoTradingServiceEntity.takeProfit,
                        "take_profit_price" to autoTradingServiceEntity.takeProfitPrice,
                        "correction_value" to autoTradingServiceEntity.correctionValue,
                        "start_date" to autoTradingServiceEntity.startDate,
                        "end_date" to autoTradingServiceEntity.endDate,
                        "trading_strategy" to autoTradingServiceEntity.tradingStrategy
                    )
                )
            )
        )

        return autoTradingServiceEntity
    }

    private suspend fun getDayCandles(
        marketId: String
    ): List<Candle> {
        return withContext(Dispatchers.IO) {
            val response =  candleUseCase.reqDaysCandle(
                market = marketId,
                to = null,
                count = 2
            )
            response.takeIf { it.isSuccess() }
                ?.successData()
                ?: listOf()
        }
    }

    private suspend fun executeBuy(
        userKey: String,
        marketId: String,
        quantityRatio: Int,
        stopLoss: Int,
        takeProfit: Int,
        correctionValue: Float,
        dayCandles: List<Candle>,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        tradingStrategy: String,
        onSkipBid: () -> Unit,
    ): Pair<Order?, Double?>? {
        val bidOrder = buyCrypto(marketId, quantityRatio, correctionValue, dayCandles, onSkipBid)
        bidOrder?.let {
            val individualOrder = reqIndividualOrder(uuid = bidOrder.uuid)

            val tradePrice = individualOrder?.trades?.firstOrNull()?.tradesPrice?.toDouble()
            saveAutoTradingHistory(
                userKey = userKey,
                uuid = bidOrder.uuid,
                bidUuid = bidOrder.uuid,
                side = Side.BID.value,
                quantityRatio = quantityRatio,
                tradingStrategy = tradingStrategy,
                stopLoss = stopLoss,
                takeProfit = takeProfit,
                correctionValue = correctionValue,
                startDate = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                endDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                individualOrder = individualOrder
            )
            return bidOrder to tradePrice
        }
        return null
    }

    private suspend fun buyCrypto(
        marketId: String,
        quantityRatio: Int,
        correctionValue: Float,
        dayCandles: List<Candle>,
        onSkipBid: () -> Unit
    ): Order? {
        return if (dayCandles.size < 2) {
            null
        } else {
            // 변동성 돌파 전략 -> 오늘 시가 + (전일 고가와 저가 변동폭 * 보정계수) 도달 시 상승 신호로 판단하여 매수 진행
            val breakoutPrice = dayCandles[0].openingPrice + ((dayCandles[1].highPrice - dayCandles[1].lowPrice) * correctionValue)
            val tradePrice = UpbitWebSocketStarter.tickersMap[marketId]?.tradePrice ?: 0.0
            val dateFormat = "${UpbitWebSocketStarter.tickersMap[marketId]?.tradeDate} ${UpbitWebSocketStarter.tickersMap[marketId]?.tradeTime}".parseDateFormat(
                DateTimeFormatter.ofPattern("yyyyMMdd HHmmss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            println("time : ${dateFormat.toSeoulTime()} , tradePrice : $tradePrice, openingPrice : ${dayCandles[0].openingPrice}, highPrice : ${dayCandles[1].highPrice}, lowPrice : ${dayCandles[1].lowPrice}, breakoutPrice : $breakoutPrice")
//            if (breakoutPrice <= tradePrice) {
            if (true) {
                val myAssets = getMyAssets()
                println("myAssets : $myAssets")
                myAssets?.firstOrNull { asset -> asset.currency.lowercase() == "krw" }?.let { krwAsset ->
                    val price = ((krwAsset.balance.toDouble() * quantityRatio / 100.0) / (1.0 + fee)).toInt()
                    println("price : $price")
                    if (price >= 5000) {
                        reqOrder(
                            marketId = marketId,
                            side = Side.BID.value,
                            volume = null,
                            price = price.toString(),
                            ordType = OrderType.PRICE.value
                        )
                    } else {
                        onSkipBid.invoke()
                        null
                    }
                }
            } else {
                null
            }
        }
    }

    @Suppress("SuspiciousIndentation")
    private suspend fun getMyAssets(): List<Asset>? {
        val assets = myAssetsUseCase.reqMyAssets()
            print("assets : $assets")
        return assets
            .takeIf { it.isSuccess() }
            ?.successData()
    }

    private suspend fun reqOrder(
        marketId: String,
        side: String,
        volume: String?,
        price: String?,
        ordType: String
    ): Order? {
        return orderUseCase.reqOrder(
            marketId = marketId,
            side = side,
            volume = volume,
            price = price,
            ordType = ordType
        )
            .takeIf { it.isSuccess() }
            ?.successData()
    }

    private suspend fun reqIndividualOrder(uuid: String): IndividualOrder? {
        delay(2000)
        return orderUseCase.reqIndividualOrders(uuid = uuid)
            .takeIf { it.isSuccess() }
            ?.successData()
    }

    private suspend fun sellCrypto(marketId: String): Order? {
        if (marketId.split("-").size != 2) {
            return null
        }

        val myAssets = getMyAssets()
        val cryptoAsset = myAssets?.firstOrNull { asset -> asset.currency.lowercase() == marketId.split("-")[1].lowercase() }
        return if (cryptoAsset == null) {
            null
        } else {
            reqOrder(
                marketId = marketId,
                side = Side.ASK.value,
                volume = cryptoAsset.balance,
                price = null,
                ordType = OrderType.MARKET.value
            )
        }
    }

    private suspend fun executeSell(
        userKey: String,
        marketId: String,
        quantityRatio: Int,
        stopLoss: Int,
        takeProfit: Int,
        correctionValue: Float,
        bidUuid: String,
        bidPrice: Double,
        now: LocalDateTime,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        tradingStrategy: String,
    ): Order? {
        var askOrder: Order? = null

        if (stopLoss != 0 && isStopLossOrTakeProfitTime(now)) {
            askOrder = isExecuteStopLoss(
                marketId = marketId,
                bidPrice = bidPrice,
                stopLoss = stopLoss
            )
        }

        if (takeProfit != 0 && askOrder == null && isStopLossOrTakeProfitTime(now)) {
            askOrder = isExecuteTakeProfit(
                marketId = marketId,
                bidPrice = bidPrice,
                takeProfit = takeProfit
            )
        }

        askOrder?.let {
            saveAutoTradingHistory(
                userKey = userKey,
                uuid = it.uuid,
                bidUuid = bidUuid,
                side = Side.ASK.value,
                quantityRatio = quantityRatio,
                tradingStrategy = tradingStrategy,
                stopLoss = stopLoss,
                takeProfit = takeProfit,
                correctionValue = correctionValue,
                startDate = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                endDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                individualOrder = reqIndividualOrder(uuid = it.uuid)
            )
        }
        return askOrder
    }

    private suspend fun isExecuteStopLoss(
        marketId: String,
        bidPrice: Double,
        stopLoss: Int
    ): Order? {
        val currentTradePrice = UpbitWebSocketStarter.tickersMap[marketId]?.tradePrice
        return if (currentTradePrice != null && bidPrice != 0.0 && currentTradePrice <= (bidPrice * ((100 - stopLoss) / 100.0))) {
            val askOrder = sellCrypto(marketId = marketId)
            return askOrder
        } else {
            null
        }
    }

    private suspend fun isExecuteTakeProfit(
        marketId: String,
        bidPrice: Double,
        takeProfit: Int
    ): Order? {
        val currentTradePrice = UpbitWebSocketStarter.tickersMap[marketId]?.tradePrice
        return if (currentTradePrice != null && bidPrice != 0.0 && currentTradePrice >= (bidPrice * (1 + (takeProfit / 100.0)))) {
            val askOrder = sellCrypto(marketId = marketId)
            return askOrder
        } else {
            null
        }
    }

    //region - 조건 확인 코드
    private fun isRequestNewCandle(today: LocalDateTime, candle: Candle): Boolean {
        val candleDate = candle.candleDateTimeKst.stringToLocalDateTime("yyyy-MM-dd HH:mm:ss")
        return if (today.hour < 9) {
            val yesterday = today.minusDays(1)
            yesterday.toLocalDate() != candleDate.toLocalDate()
        } else {
            today.toLocalDate() != candleDate.toLocalDate()
        }
    }

    private fun isBidTime(now: LocalDateTime): Boolean {
        val start = now.withHour(8).withMinute(58).withSecond(59)
        val end = now.withHour(9).withMinute(1).withSecond(0)
        return now.isBefore(start) || now.isAfter(end)
    }


    private fun isAskTime(now: LocalDateTime): Boolean {
        val start = now.withHour(8).withMinute(59).withSecond(0)
        val end = now.withHour(8).withMinute(59).withSecond(59)
        return now.isAfter(start) && now.isBefore(end)
    }

    private fun isCanBid(order: Order?) = order == null

    private fun isCanAsk(order: Order?) = order != null

    private fun isStopLossOrTakeProfitTime(now: LocalDateTime): Boolean {
        val start = now.withHour(8).withMinute(58).withSecond(59)
        val end = now.withHour(9).withMinute(2).withSecond(0)
        return start.isBefore(now) || end.isAfter(now)
    }

    //endregion

    @Transactional
    fun saveLog(log: String): AutoTradingLogEntity = logRepository.save(AutoTradingLogEntity(log = log))

    @Transactional
    fun saveAutoTradingHistory(
        userKey: String,
        uuid: String,
        bidUuid: String,
        side: String,
        quantityRatio: Int,
        tradingStrategy: String,
        stopLoss: Int,
        takeProfit: Int,
        correctionValue: Float,
        startDate: String,
        endDate: String,
        individualOrder: IndividualOrder?
    ) {

        val existing = historyPeriodRepository.findByStartDateWithHistories(userKey = userKey, startDate = startDate)
        if (existing == null) {
            val period = AutoTradingHistoryPeriodEntity(userKey = userKey, startDate = startDate, endDate = endDate)
            val history = AutoTradingHistoryEntity(
                uuid = uuid,
                bidUuid = bidUuid,
                individualUuid = individualOrder?.uuid ?: "",
                userKey = userKey,
                side = side,
                quantityRatio = quantityRatio,
                tradingStrategy = tradingStrategy,
                stopLoss = stopLoss,
                takeProfit = takeProfit,
                correctionValue = correctionValue,
                startDate = startDate,
                endDate = endDate,
                createdAt = OffsetDateTime.now(),
                period = period
            )
            period.histories.add(history)
            historyPeriodRepository.save(period)
        } else {
            val history = AutoTradingHistoryEntity(
                uuid = uuid,
                bidUuid = bidUuid,
                individualUuid = individualOrder?.uuid ?: "",
                userKey = userKey,
                side = side,
                quantityRatio = quantityRatio,
                tradingStrategy = tradingStrategy,
                stopLoss = stopLoss,
                takeProfit = takeProfit,
                correctionValue = correctionValue,
                startDate = startDate,
                endDate = endDate,
                createdAt = OffsetDateTime.now(),
                period = existing
            )
            existing.histories.add(history)
            historyPeriodRepository.save(existing)
        }


        individualOrder?.let {
            val individualOrderEntity = IndividualOrderEntity(
                uuid = individualOrder.uuid,
                side = individualOrder.side,
                userKey = userKey,
                ordType = individualOrder.ordType,
                price = individualOrder.price?.let { BigDecimal(it) },
                state = individualOrder.state,
                market = individualOrder.market,
                createdAt = OffsetDateTime.parse(individualOrder.createdAt),
                volume = individualOrder.volume?.let { BigDecimal(it) },
                remainingVolume = individualOrder.remainingVolume?.let { BigDecimal(it) },
                reservedFee = BigDecimal(individualOrder.reservedFee),
                remainingFee = BigDecimal(individualOrder.remainingFee),
                paidFee = BigDecimal(individualOrder.paidFee),
                locked = BigDecimal(individualOrder.locked),
                executedVolume = BigDecimal(individualOrder.executedVolume),
                tradesCount = individualOrder.tradesCount,
                timeInForce = individualOrder.timeInForce,
                identifier = individualOrder.identifier
            )

            val tradieEntities = it.trades?.map { trade ->
                TradeEntity(
                    tradesUuid = trade.tradesUuid,
                    tradesMarket = trade.tradesMarket,
                    tradesPrice = BigDecimal(trade.tradesPrice),
                    tradesVolume = BigDecimal(trade.tradesVolume),
                    tradesFunds = BigDecimal(trade.tradesFunds),
                    tradesSide = trade.tradesSide,
                    tradesCreatedAt = trade.tradesCreatedAt,
                    userKey = userKey,
                    order = individualOrderEntity
                )
            } ?: listOf()
            individualOrderEntity.trades.addAll(tradieEntities)
            individualOrderRepository.save(individualOrderEntity)
        }
    }

    @Transactional
    fun getTradingHistory(userKey: String): List<TradingData> {
        val autoTradingServiceEntity = autoTradingServiceRepository.findByUserKey(userKey = userKey)
        val startDate = autoTradingServiceEntity.startDate

        val list =  if (startDate == null) {
            listOf()
        } else {
            historyRepository
                .findAllByStartDate(userKey = userKey, startDate = startDate.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).map { entity ->
                    val individualOrder = individualOrderRepository.findByBidUuid(userKey = userKey, bidUuid = entity.individualUuid).let {
                        IndividualOrder(
                            uuid = it?.uuid ?: "",
                            side = it?.side ?: "",
                            ordType = it?.ordType ?: "",
                            price = it?.price?.toString(),
                            state = it?.state ?: "",
                            market = it?.market ?: "",
                            createdAt = it?.createdAt?.toString() ?: "",
                            volume = it?.volume?.toString(),
                            remainingVolume = it?.remainingVolume?.toString(),
                            reservedFee = it?.reservedFee?.toString() ?: "",
                            remainingFee = it?.remainingFee?.toString() ?: "",
                            paidFee = it?.paidFee?.toString() ?: "",
                            locked = it?.locked?.toString() ?: "",
                            executedVolume = it?.executedVolume?.toString() ?: "",
                            tradesCount = it?.tradesCount ?: 0,
                            timeInForce = it?.timeInForce,
                            identifier = it?.identifier,
                            trades = it?.trades?.map { tradeEntity ->
                                Trade(
                                    tradesUuid = tradeEntity.tradesUuid,
                                    tradesMarket = tradeEntity.tradesMarket,
                                    tradesPrice = tradeEntity.tradesPrice.toString(),
                                    tradesVolume = tradeEntity.tradesVolume.toString(),
                                    tradesFunds = tradeEntity.tradesFunds.toString(),
                                    tradesSide = tradeEntity.tradesSide,
                                    tradesCreatedAt = tradeEntity.tradesCreatedAt.toString().parseDateFormat(
                                        inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"),
                                        outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                    )
                                )
                            }
                        )
                    }

                    TradingData(
                        uuid = entity.uuid,
                        bidUuid = entity.bidUuid,
                        quantityRatio = entity.quantityRatio,
                        tradingStrategy = entity.tradingStrategy,
                        stopLoss = entity.stopLoss,
                        takeProfit = entity.takeProfit,
                        correctionValue = entity.correctionValue,
                        endDateTime = entity.endDate,
                        order = individualOrder
                    )
                }
        }

        autoTradingHandler.sendMessageToClient(
            userKey = userKey,
            map = mapOf(
                "mode" to AutoTradingConstants.HISTORY,
                "data" to list
            )
        )

        return list
    }
}