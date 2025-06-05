package st.seno.autotrading.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.os.Binder
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import st.seno.autotrading.R
import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.model.Order
import st.seno.autotrading.data.network.model.Ticker
import st.seno.autotrading.data.network.model.isSuccess
import st.seno.autotrading.data.network.model.successData
import st.seno.autotrading.data.network.response_model.IndividualOrder
import st.seno.autotrading.data.network.socket.RxSocketClient
import st.seno.autotrading.data.network.socket.SockResponse
import st.seno.autotrading.di.Qualifiers
import st.seno.autotrading.domain.CandleUseCase
import st.seno.autotrading.domain.MyAssetsUseCase
import st.seno.autotrading.domain.OrderUseCase
import st.seno.autotrading.extensions.formatRealPrice
import st.seno.autotrading.extensions.parseDateFormat
import st.seno.autotrading.extensions.stringToLocalDateTime
import st.seno.autotrading.extensions.toDate
import st.seno.autotrading.extensions.toLocalDateTime
import st.seno.autotrading.extensions.toSeoulTime
import st.seno.autotrading.keyname.KeyName
import st.seno.autotrading.model.OrderType
import st.seno.autotrading.model.Side
import st.seno.autotrading.prefs.PrefsManager
import st.seno.autotrading.ui.main.MainActivity
import st.seno.autotrading.ui.main.MainViewModel
import timber.log.Timber
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
        release()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        super.onDestroy()
    }

    private fun release() {
        job?.cancel()
    }

    @SuppressLint("ForegroundServiceType", "ObsoleteSdkInt")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.e("onStartCommand")
        val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        val exception = Exception("onStartCommand --> date: ${now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}\n" +
                "isRunningAutoTradingService : ${PrefsManager.AutoTrading.isRunningTradingService}\n" +
                "marketId : ${intent?.getStringExtra(KeyName.Intent.MARKET_ID)}, marketId cache : ${PrefsManager.AutoTrading.marketId}\n" +
                "correctionValue : ${intent?.getFloatExtra(KeyName.Intent.CORRECTION_VALUE, 0f)}, correctionValue cache : ${PrefsManager.AutoTrading.correctionValue}\n" +
                "endDate : ${intent?.getLongExtra(KeyName.Intent.END_DATE, 0L)}, endDate cache : ${PrefsManager.AutoTrading.endDate}"
        )
        FirebaseCrashlytics.getInstance().recordException(exception)
        FirebaseCrashlytics.getInstance().recordException(Exception("????"))

        Timber.e("onStartCommand : ${PrefsManager.AutoTrading.isRunningTradingService}")
        if (PrefsManager.AutoTrading.isRunningTradingService) return START_STICKY
        PrefsManager.AutoTrading.isRunningTradingService = true

        val marketId = intent?.getStringExtra(KeyName.Intent.MARKET_ID)
            ?: PrefsManager.AutoTrading.marketId

        val quantityRatio = intent?.getIntExtra(KeyName.Intent.QUANTITY_RATIO, 0)
            ?: PrefsManager.AutoTrading.quantityRatio

        val stopLoss = intent?.getIntExtra(KeyName.Intent.STOP_LOSS, 0)
            ?: PrefsManager.AutoTrading.stopLoss

        val takeProfit = intent?.getIntExtra(KeyName.Intent.TAKE_PROFIT, 0)
            ?: PrefsManager.AutoTrading.takeProfit

        val correctionValue = intent?.getFloatExtra(KeyName.Intent.CORRECTION_VALUE, 0f)
            ?: PrefsManager.AutoTrading.correctionValue

        val startDate = intent?.getLongExtra(KeyName.Intent.START_DATE, 0L)
            ?: PrefsManager.AutoTrading.startDate

        val endDate = intent?.getLongExtra(KeyName.Intent.END_DATE, 0L)
            ?: PrefsManager.AutoTrading.endDate

        val tradingStrategy = intent?.getStringExtra(KeyName.Intent.TRADING_STRATEGY)
            ?: PrefsManager.AutoTrading.tradingStrategy

        val tradingMode = intent?.getStringExtra(KeyName.Intent.CURRNET_TRADING_MODE)
            ?: PrefsManager.AutoTrading.tradingMode

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
                startDate = startDate.toDate("yyyy-MM-dd"),
                endDateTime = endDate.toLocalDateTime(),
                tradingStrategy = tradingStrategy
            )
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
        startDate: String,
        endDateTime: LocalDateTime,
        tradingStrategy: String,
    ) {
        var bidOrder: Order? = PrefsManager.Data.bidOrder
        var bidPrice: Double = PrefsManager.Data.tradePrice
        var dayCandles: List<Candle> = listOf()
        var isSkipBid = false

        job = CoroutineScope(ioDispatcher).launch {
            while (true) {
                Timber.e("endDateTime : ${endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}")
                val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                if (now.isAfter(endDateTime) && bidOrder == null) {
                    Timber.e("end")
                    break
                }

                dayCandles = if (dayCandles.isEmpty() || dayCandles.firstOrNull()?.let { isRequestNewCandle(now, it) } == true) {
                    getDayCandles(marketId)
                } else {
                    dayCandles
                }

                if (dayCandles.isNotEmpty()) {
                    Timber.e("today -> ${dayCandles[0].candleDateTimeKst}, bidOrder: $bidOrder, bidPrice: $bidPrice, isSkipBid : $isSkipBid")
                }

                // 매수 가능 체크
                if (isBidTime(now = now) && isCanBid(order = bidOrder) && !isSkipBid) {
                    val (order, tradePrice) = executeBuy(
                        marketId = marketId,
                        quantityRatio = quantityRatio,
                        stopLoss = stopLoss,
                        takeProfit = takeProfit,
                        correctionValue = correctionValue,
                        dayCandles = dayCandles,
                        startDate = startDate,
                        endDateTime = endDateTime,
                        tradingStrategy = tradingStrategy,
                        onSkipBid = { isSkipBid = true },
                    ) ?: Pair(null, null)

                    bidOrder = order.also { PrefsManager.Data.bidOrder = it }
                    bidPrice = (tradePrice ?: 0.0).also { PrefsManager.Data.tradePrice = it }

                    if (bidPrice != 0.0) {
                        PrefsManager.AutoTrading.apply {
                            this.stopLoss = stopLoss
                            this.stopLossPrice = (bidPrice * ((100 - stopLoss) / 100.0)).formatRealPrice()
                            this.takeProfit = takeProfit
                            this.takeProfitPrice = (bidPrice * (1 + (takeProfit / 100.0))).formatRealPrice()
                        }
                    }
                    Timber.e("bid : $bidOrder")
                }

                // StopLoss 혹은 TakeProfit 체크
                if (isCanAsk(order = bidOrder) && bidPrice != 0.0) {
                    executeSell(
                        marketId = marketId,
                        quantityRatio = quantityRatio,
                        stopLoss = stopLoss,
                        takeProfit = takeProfit,
                        correctionValue = correctionValue,
                        bidUuid = bidOrder?.uuid ?: "",
                        bidPrice = bidPrice,
                        now = now,
                        startDate = startDate,
                        endDateTime = endDateTime,
                        tradingStrategy = tradingStrategy
                    )?.also {
                        bidOrder = null
                        PrefsManager.Data.bidOrder = null

                        isSkipBid = true.also { PrefsManager.Data.isSkipBid = true }
                        bidPrice = 0.0.also { PrefsManager.Data.tradePrice = 0.0 }
                        dayCandles = listOf()
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
                                startDate = startDate,
                                endDateTime = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                uuid = askOrder.uuid,
                                bidUuid = bidOrder?.uuid ?: "",
                                individualOrder = reqIndividualOrder(uuid = askOrder.uuid)
                            )

                            bidOrder = null
                            PrefsManager.Data.bidOrder = null

                            bidPrice = 0.0.also { PrefsManager.Data.tradePrice = 0.0 }
                            dayCandles = listOf()
                        }
                    }
                    isSkipBid = false.also { PrefsManager.Data.isSkipBid = false }
                }

                delay(timeMillis = 1000)
            }

            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
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

    private fun isStopLossOrTakeProfitTime(now: LocalDateTime): Boolean {
        val start = now.withHour(8).withMinute(58).withSecond(59)
        val end = now.withHour(9).withMinute(2).withSecond(0)
        return start.isBefore(now) || end.isAfter(now)
    }

    private fun isRequestNewCandle(today: LocalDateTime, candle: Candle): Boolean {
        val candleDate = candle.candleDateTimeKst.stringToLocalDateTime("yyyy-MM-dd HH:mm:ss")
        return if (today.hour < 9) {
            val yesterday = today.minusDays(1)
            yesterday.toLocalDate() != candleDate.toLocalDate()
        } else {
            today.toLocalDate() != candleDate.toLocalDate()
        }
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
            val tradePrice = MainViewModel.tickersMap.value[marketId]?.tradePrice ?: 0.0
            val dateFormat = "${MainViewModel.tickersMap.value[marketId]?.tradeDate} ${MainViewModel.tickersMap.value[marketId]?.tradeTime}".parseDateFormat(
                DateTimeFormatter.ofPattern("yyyyMMdd HHmmss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )

            Timber.e("time : ${dateFormat.toSeoulTime()} , tradePrice : $tradePrice, openingPrice : ${dayCandles[0].openingPrice}, highPrice : ${dayCandles[1].highPrice}, lowPrice : ${dayCandles[1].lowPrice}, breakoutPrice : $breakoutPrice")
            if (breakoutPrice <= tradePrice) {
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
                Timber.e("555")
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
        startDate: String,
        endDateTime: LocalDateTime,
        tradingStrategy: String,
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
                startDate = startDate,
                endDateTime = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                uuid = bidOrder.uuid,
                bidUuid = bidOrder.uuid,
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
        bidUuid: String,
        bidPrice: Double,
        now: LocalDateTime,
        startDate: String,
        endDateTime: LocalDateTime,
        tradingStrategy: String
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
                startDate = startDate,
                endDateTime = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                uuid = it.uuid,
                bidUuid = bidUuid,
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
        val currentTradePrice = MainViewModel.tickersMap.value[marketId]?.tradePrice
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
        val currentTradePrice = MainViewModel.tickersMap.value[marketId]?.tradePrice
        return if (currentTradePrice != null && bidPrice != 0.0 && currentTradePrice >= (bidPrice * (1 + (takeProfit / 100.0)))) {
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
        startDate: String,
        endDateTime: String,
        uuid: String,
        bidUuid: String,
        individualOrder: IndividualOrder?
    ) {
        if (individualOrder != null) {
            val map = mapOf(
                "uuid" to uuid,
                "bidUuid" to bidUuid,
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
                .collection(startDate)
                .document(System.currentTimeMillis().toString())
                .set(map)
                .await()
        }
    }
}