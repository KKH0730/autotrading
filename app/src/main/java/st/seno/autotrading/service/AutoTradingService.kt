package st.seno.autotrading.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.model.Order
import st.seno.autotrading.data.network.model.isSuccess
import st.seno.autotrading.data.network.model.successData
import st.seno.autotrading.data.network.response_model.IndividualOrder
import st.seno.autotrading.di.Qualifiers
import st.seno.autotrading.domain.CandleUseCase
import st.seno.autotrading.domain.MyAssetsUseCase
import st.seno.autotrading.domain.OrderUseCase
import st.seno.autotrading.extensions.formatRealPrice
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.keyname.KeyName
import st.seno.autotrading.model.OrderType
import st.seno.autotrading.model.Side
import st.seno.autotrading.ui.main.MainActivity
import st.seno.autotrading.ui.main.MainViewModel
import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class AutoTradingService : Service() {

    @Inject
    lateinit var orderUseCase: OrderUseCase

    @Inject
    lateinit var myAssetUseCase: MyAssetsUseCase

    @Inject
    lateinit var candleUseCase: CandleUseCase

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    @Qualifiers.IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    private val CHANNEL_ID = "TradingServiceChannel"
    private val fee: Double = 0.0005 // 업비트 수수료
    private var job: Job? = null

    override fun onDestroy() {
        FirebaseCrashlytics.getInstance().recordException(Exception("destroy 22: $tradingStartDate"))

        isRunningAutoTradingService.value = false
        tradingStartDate = ""
        tradingEndDate = ""
        tradingStrategy = ""
        tradingStopLoss = "" to ""
        tradingTakeProfit = "" to ""
        job?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        super.onDestroy()
    }

    @SuppressLint("ForegroundServiceType", "ObsoleteSdkInt")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isRunningAutoTradingService.value) return START_STICKY
        isRunningAutoTradingService.value = true


        intent?.let {
            val marketId: String = it.getStringExtra(KeyName.Intent.MARKET_ID) ?: ""
            val quantityRatio: Int = it.getIntExtra(KeyName.Intent.QUANTITY_RATIO, 0)
            val tradingStrategy: String = it.getStringExtra(KeyName.Intent.TRADING_STRATEGY) ?: ""
            val stopLoss: Int = it.getIntExtra(KeyName.Intent.STOP_LOSS, 0)
            val takeProfit: Int = it.getIntExtra(KeyName.Intent.TAKE_PROFIT, 0)
            val correctionValue: Float = it.getFloatExtra(KeyName.Intent.CORRECTION_VALUE, 0f)
            val endDate: Long = it.getLongExtra(KeyName.Intent.END_DATE,0L)
            val tradingMode: String = it.getStringExtra(KeyName.Intent.CURRNET_TRADING_MODE) ?: ""

            val endDateTime = Instant.ofEpochMilli(endDate)
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime()
                .plusDays(1)
                .withHour(8)
                .withMinute(59)
                .withSecond(59)

            tradingMarketId = marketId
            tradingStartDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            tradingEndDate = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            AutoTradingService.tradingStrategy = tradingStrategy
            tradingStopLoss = stopLoss.toString() to ""
            tradingTakeProfit = takeProfit.toString() to ""

            if (marketId.isNotEmpty() && endDate != 0L) {
                createNotificationChannel()
                val requestCode = (System.currentTimeMillis() / 1000).toInt()
                val notification = createNotification(requestCode = requestCode)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startForeground(requestCode, notification, FOREGROUND_SERVICE_TYPE_DATA_SYNC)
                } else {
                    startForeground(requestCode, notification)
                }
                startTrading(
                    marketId = marketId,
                    quantityRatio = quantityRatio,
                    stopLoss = stopLoss,
                    takeProfit = takeProfit,
                    correctionValue = correctionValue,
                    endDateTime = endDateTime
                )
            }
        }

        return START_STICKY
    }

    private fun createNotification(requestCode: Int): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("tabIndex", 2)
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setColor(ContextCompat.getColor(this, R.color.FF18C942))
            .setContentTitle(getString(R.string.auto_trading_service_notification_title))
            .setContentText(getString(R.string.auto_trading_service_notification_content))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setAutoCancel(false)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentIntent(PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
            .build()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Trading Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("SimpleDateFormat")
    private fun startTrading(
        marketId: String,
        quantityRatio: Int,
        stopLoss: Int,
        takeProfit: Int,
        correctionValue: Float,
        endDateTime: LocalDateTime,
    ) {
        var bidOrder: Order? = null
        var bidPrice: Double? = null
        var dayCandles: List<Candle> = listOf()
        var isSkipBid = false

        job = CoroutineScope(ioDispatcher).launch {
            while (true) {
                Timber.e("endDateTime : ${endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}")
                val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                if (now.isAfter(endDateTime) && bidOrder == null) {
                    break
                }
                dayCandles = dayCandles.ifEmpty { getDayCandles(marketId = marketId) }

                // 매수 가능 체크
                if (isBidTime(now = now) && isCanBid(order = bidOrder) && !isSkipBid) {
                    val (order, tradePrice) = executeBuy(
                        marketId = marketId,
                        quantityRatio = quantityRatio,
                        stopLoss = stopLoss,
                        takeProfit = takeProfit,
                        correctionValue = correctionValue,
                        dayCandles = dayCandles,
                        endDateTime = endDateTime,
                        onSkipBid = { isSkipBid = true },
                    ) ?: Pair(null, null)

                    bidOrder = order
                    bidPrice = tradePrice

                    bidPrice?.let {
                        tradingStopLoss = stopLoss.toString() to (it * ((100 - stopLoss) / 100.0)).formatRealPrice()
                        tradingTakeProfit = takeProfit.toString() to (it * (1 + (takeProfit / 100.0))).formatRealPrice()
                    }
                    Timber.e("bid : $bidOrder")
                }

                // StopLoss 혹은 TakeProfit 체크
                if (isCanAsk(order = bidOrder) && bidPrice != null) {
                    bidPrice?.let {
                        executeSell(
                            marketId = marketId,
                            quantityRatio = quantityRatio,
                            stopLoss = stopLoss,
                            takeProfit = takeProfit,
                            correctionValue = correctionValue,
                            bidPrice = it,
                            now = now,
                            endDateTime = endDateTime
                        )
                    }
                }

                // 매도 가능 체크
                if (isAskTime(now = now)) {
                    if (isCanAsk(order = bidOrder)) {
                        sellCrypto(marketId = marketId)?.let { askOrder ->
                            Timber.e("ask : $askOrder")
                            reqUpdateTradingData(
                                quantityRatio = quantityRatio,
                                tradingStrategy = tradingStrategy,
                                stopLoss = stopLoss,
                                takeProfit = takeProfit,
                                correctionValue = correctionValue,
                                endDateTime = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                uuid = askOrder.uuid,
                                individualOrder = reqIndividualOrder(uuid = askOrder.uuid)
                            )
                        }
                    }
                    isSkipBid = false
                }

                if (isInitialTime(now = now)) {
                    bidOrder = null
                    bidPrice = null
                    dayCandles = listOf()
                }
                delay(timeMillis = 1000)
            }
            FirebaseCrashlytics.getInstance().recordException(Exception("destroy 11: $tradingStartDate"))
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun isBidTime(now: LocalDateTime): Boolean {
        val start = now.withHour(8).withMinute(57).withSecond(59)
        val end = now.withHour(9).withMinute(0).withSecond(0)
        return now.isBefore(start) || now.isAfter(end)
    }

    private fun isAskTime(now: LocalDateTime): Boolean {
        val start = now.withHour(8).withMinute(58).withSecond(0)
        val end = now.withHour(8).withMinute(59).withSecond(59)
        return now.isAfter(start) && now.isBefore(end)
    }

    private fun isStopLossOrTakeProfitTime(now: LocalDateTime): Boolean {
        val start = now.withHour(8).withMinute(57).withSecond(59)
        val end = now.withHour(9).withMinute(0).withSecond(0)
        return start.isBefore(now) || end.isAfter(now)
    }

    private fun isInitialTime(now: LocalDateTime): Boolean {
        val start = now.withHour(8).withMinute(58).withSecond(58)
        val end = now.withHour(8).withMinute(59).withSecond(59)
        return now.isAfter(start) && now.isBefore(end)
    }

    private fun isCanBid(order: Order?) = order == null

    private fun isCanAsk(order: Order?) = order != null

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
            Timber.e("breakoutPrice : $breakoutPrice")
            if (breakoutPrice <= dayCandles[0].tradePrice) {
                val myAssets = getMyAssets()
                myAssets?.firstOrNull { asset -> asset.currency.lowercase() == getString(R.string.krw) }?.let { krwAsset ->
                    val price = ((krwAsset.balance.toDouble() * quantityRatio / 100.0) / (1.0 + fee)).toInt()
                    Timber.e("price : $price")
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

    private suspend fun executeBuy(
        marketId: String,
        quantityRatio: Int,
        stopLoss: Int,
        takeProfit: Int,
        correctionValue: Float,
        dayCandles: List<Candle>,
        endDateTime: LocalDateTime,
        onSkipBid: () -> Unit,
    ): Pair<Order?, Double?>? {
        val bidOrder = buyCrypto(marketId, quantityRatio, correctionValue, dayCandles, onSkipBid)
        bidOrder?.let {
            val individualOrder = reqIndividualOrder(uuid = bidOrder.uuid)

            val tradePrice = individualOrder?.trades?.firstOrNull()?.tradesPrice?.toDouble()
            reqUpdateTradingData(
                quantityRatio = quantityRatio,
                tradingStrategy = tradingStrategy,
                stopLoss = stopLoss,
                takeProfit = takeProfit,
                correctionValue = correctionValue,
                endDateTime = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                uuid = bidOrder.uuid,
                individualOrder = reqIndividualOrder(uuid = bidOrder.uuid)
            )
            return bidOrder to tradePrice
        }
        return null
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
        marketId: String,
        quantityRatio: Int,
        stopLoss: Int,
        takeProfit: Int,
        correctionValue: Float,
        bidPrice: Double,
        now: LocalDateTime,
        endDateTime: LocalDateTime
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
            reqUpdateTradingData(
                quantityRatio = quantityRatio,
                tradingStrategy = tradingStrategy,
                stopLoss = stopLoss,
                takeProfit = takeProfit,
                correctionValue = correctionValue,
                endDateTime = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                uuid = it.uuid,
                individualOrder = reqIndividualOrder(uuid = it.uuid)
            )
        }
        return askOrder
    }

    private suspend fun isExecuteStopLoss(
        marketId: String,
        bidPrice: Double?,
        stopLoss: Int
    ): Order? {
        val currentTradePrice = MainViewModel.tickersMap.value[marketId]?.tradePrice
        return if (currentTradePrice != null && bidPrice != null && currentTradePrice <= (bidPrice * ((100 - stopLoss) / 100.0))) {
            val askOrder = sellCrypto(marketId = marketId)
            return askOrder
        } else {
            null
        }
    }

    private suspend fun isExecuteTakeProfit(
        marketId: String,
        bidPrice: Double?,
        takeProfit: Int
    ): Order? {
        val currentTradePrice = MainViewModel.tickersMap.value[marketId]?.tradePrice
        return if (currentTradePrice != null && bidPrice != null && currentTradePrice >= (bidPrice * (1 + (takeProfit / 100.0)))) {
            val askOrder = sellCrypto(marketId = marketId)
            return askOrder
        } else {
            null
        }
    }

    private suspend fun getDayCandles(
        marketId: String
    ): List<Candle> {
        return withContext(ioDispatcher) {
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

    private suspend fun getMyAssets(): List<Asset>? {
        return myAssetUseCase.reqMyAssets()
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
        delay(1000)
        return orderUseCase.reqIndividualOrders(uuid = uuid)
            .takeIf { it.isSuccess() }
            ?.successData()
    }

    private suspend fun reqUpdateTradingData(
        quantityRatio: Int,
        tradingStrategy: String,
        stopLoss: Int,
        takeProfit: Int,
        correctionValue: Float,
        endDateTime: String,
        uuid: String,
        individualOrder: IndividualOrder?
    ) {
        if (individualOrder != null) {
            val map = mapOf(
                "uuid" to uuid,
                "order" to individualOrder,
                "quantityRatio" to quantityRatio,
                "tradingStrategy" to tradingStrategy,
                "stopLoss" to stopLoss,
                "takeProfit" to takeProfit,
                "correctionValue" to correctionValue,
                "endDateTime" to endDateTime,
            )
            firestore.collection(KeyName.Firestore.TRADING_COLLECTION)
                .document(KeyName.Firestore.AUTO_TRADING_DOCUMENT)
                .collection(tradingStartDate)
                .document(System.currentTimeMillis().toString())
                .set(map)
                .await()
        }
    }

    companion object {
        val isRunningAutoTradingService: MutableStateFlow<Boolean> = MutableStateFlow(false)
        var tradingMarketId: String = ""
        var tradingStartDate: String = ""
        var tradingEndDate: String = ""
        var tradingStrategy: String = ""
        var tradingStopLoss: Pair<String, String> = "" to ""
        var tradingTakeProfit: Pair<String, String> = "" to ""
    }
}