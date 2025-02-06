package st.seno.autotrading

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.icu.util.Calendar
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.keytalkai.lewis.di.Qualifiers
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import st.seno.autotrading.data.network.model.Asset
import st.seno.autotrading.data.network.model.Candle
import st.seno.autotrading.data.network.model.Order
import st.seno.autotrading.data.network.model.isSuccess
import st.seno.autotrading.data.network.model.successData
import st.seno.autotrading.domain.CandleUseCase
import st.seno.autotrading.domain.MyAssetsUseCase
import st.seno.autotrading.domain.OrderUseCase
import st.seno.autotrading.extensions.calendarToLocalDateTime
import st.seno.autotrading.extensions.formatedDate
import st.seno.autotrading.extensions.truncateToXDecimalPlaces
import st.seno.autotrading.keyname.KeyName
import st.seno.autotrading.model.OrderType
import st.seno.autotrading.model.Side
import st.seno.autotrading.ui.main.MainActivity
import st.seno.autotrading.ui.main.MainViewModel
import st.seno.autotrading.ui.main.home.market_overview.marketOverviewTabs
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class AutoTradingService : Service() {

    @Inject
    lateinit var orderUseCase: OrderUseCase

    @Inject
    lateinit var myAssetUseCase: MyAssetsUseCase

    @Inject
    lateinit var candleUseCase: CandleUseCase

    @Inject
    @Qualifiers.IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    private val CHANNEL_ID = "TradingServiceChannel"
    private val k = 0.8f // 보정계수
    private val stopLossPercent = 0.05 // 손절 비율 (5%)
    private val takeProfitPercent = 0.1 // 익절 비율 (10%)
    private val fee: Double = 0.05

    override fun onDestroy() {
        isRunningAutoTradingService.value = false
        super.onDestroy()
    }

    @SuppressLint("ForegroundServiceType", "ObsoleteSdkInt")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isRunningAutoTradingService.value) return START_STICKY
        isRunningAutoTradingService.value = true


        intent?.let {
            val marketId: String = it.getStringExtra(KeyName.Intent.MARKET_ID) ?: ""
            val quantityRatio: Int = it.getIntExtra(KeyName.Intent.QUANTITY_RATIO, 0)
            val stopLoss: Int = it.getIntExtra(KeyName.Intent.STOP_LOSS, 0)
            val takeProfit: Int = it.getIntExtra(KeyName.Intent.TAKE_PROFIT, 0)
            val endDate: Long = it.getLongExtra(KeyName.Intent.END_DATE,0L)
            val tradingMode: String = it.getStringExtra(KeyName.Intent.CURRNET_TRADING_MODE) ?: ""

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
                    endDate = endDate
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
            .setSmallIcon(android.R.drawable.ic_dialog_info)
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
        endDate: Long
    ) {
        var bidOrder: Order? = null
        var bidPrice: Double? = null
        var isAlreadyExecuteStopLoss = false
        var isAlreadyExecuteTakeProfit = false

        val endCalendar = java.util.Calendar.getInstance().apply {
            timeInMillis = endDate
            add(Calendar.DATE, 1)
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }

        Timber.e("Date1 : ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date().apply { time = endCalendar.timeInMillis })}")

        CoroutineScope(ioDispatcher).launch {
            while (true) {
                Timber.e("!!!")
                val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                val endDateTime = endCalendar.calendarToLocalDateTime(zoneId = ZoneId.of("Asia/Seoul"))

                if (now.isAfter(endDateTime) && bidOrder == null) {
                    break
                }

                if (isBidTime(now = now) && bidOrder == null && !isAlreadyExecuteStopLoss && !isAlreadyExecuteTakeProfit) {
                    Timber.e("bid")
                    bidOrder = buyCrypto(marketId = marketId, quantityRatio = quantityRatio)
                    bidPrice = MainViewModel.tickersMap.value[marketId]?.tradePrice
                    Timber.e("bid : $bidOrder")
                }

                if (bidOrder != null && bidPrice != null && stopLoss != 0 && takeProfit != 0) {
                    if (isStopLossOrTakeProfitTime(now = now) && isExecuteStopLoss(marketId = marketId, bidPrice = bidPrice, stopLoss = stopLoss)) {
                        Timber.e("stopLoss")
                        bidOrder = null
                        bidPrice = null
                        isAlreadyExecuteStopLoss = true
                    }

                    if (isStopLossOrTakeProfitTime(now = now) && isExecuteTakeProfit(marketId = marketId, bidPrice = bidPrice, takeProfit = takeProfit)) {
                        Timber.e("takeProfit")
                        bidOrder = null
                        bidPrice = null
                        isAlreadyExecuteTakeProfit = true
                    }
                }

                if (isAskTime(now = now)) {
                    if (bidOrder != null && !isAlreadyExecuteStopLoss && !isAlreadyExecuteTakeProfit) {
                        Timber.e("ask")
                        val askOrder = sellCrypto(marketId = marketId)
                        Timber.e("ask : $askOrder")
                        if (askOrder != null) {
                            bidOrder = null
                            bidPrice = null
                        }
                    }

                    isAlreadyExecuteStopLoss = false
                    isAlreadyExecuteTakeProfit = false
                }
                delay(timeMillis = 1000)
            }
            stopSelf()
        }
    }

    private fun isBidTime(now: LocalDateTime): Boolean {
        val start = now.withHour(8).withMinute(0).withSecond(0)
        val end = now.withHour(8).withMinute(59).withSecond(59)
        return now.isBefore(start) || now.isAfter(end)
    }

    private fun isAskTime(now: LocalDateTime): Boolean {
        val start = now.withHour(8).withMinute(0).withSecond(0)
        val end = now.withHour(8).withMinute(59).withSecond(59)
        return now.isAfter(start) && now.isBefore(end)
    }

    private fun isStopLossOrTakeProfitTime(now: LocalDateTime): Boolean {
        val start = now.withHour(8).withMinute(0).withSecond(0)
        val end = now.withHour(8).withMinute(59).withSecond(59)
        return start.isBefore(now) || end.isAfter(now)
    }

    private suspend fun buyCrypto(
        marketId: String,
        quantityRatio: Int
    ): Order? {
        val myAssets = getMyAssets()
        val krwAsset = myAssets?.firstOrNull { asset -> asset.currency.lowercase() == getString(R.string.krw) }
        val dayCandles = getDayCandles(marketId = marketId)

        return if (krwAsset == null || dayCandles == null || dayCandles.size < 2) {
            null
        } else {
            Timber.e("buyCrypto : $krwAsset")

            // 변동성 돌파 전략 -> 오늘 시가 + (전일 고가와 저가 변동폭 * 보정계수) 도달 시 상승 신호로 판단하여 매수 진행
            val breakoutPrice = dayCandles[0].openingPrice + ((dayCandles[1].highPrice - dayCandles[1].lowPrice) * k)
            if (breakoutPrice >= dayCandles[0].tradePrice) {
                Timber.e("breakoutPrice : $breakoutPrice")
                reqOrder(
                    marketId = marketId,
                    side = Side.BID.value,
                    volume = null,
                    price = (krwAsset.balance.toDouble() * ((quantityRatio - fee ) / 100)).truncateToXDecimalPlaces(x = 2.0).toString(),
                    ordType = OrderType.PRICE.value
                )
            } else {
                null
            }
        }
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
            Timber.e("sellCrypto : $cryptoAsset")
            reqOrder(
                marketId = marketId,
                side = Side.ASK.value,
                volume = cryptoAsset.balance,
                price = null,
                ordType = OrderType.MARKET.value
            )
        }
    }

    private suspend fun isExecuteStopLoss(
        marketId: String,
        bidPrice: Double?,
        stopLoss: Int
    ): Boolean {
        Timber.e("isExecuteStopLoss stopLoss : $stopLoss")
        val currentTradePrice = MainViewModel.tickersMap.value[marketId]?.tradePrice
        if (currentTradePrice != null && bidPrice != null && currentTradePrice <= (bidPrice * ((100 - stopLoss) / 100.0))) {
            val askOrder = sellCrypto(marketId = marketId)
            return askOrder != null
        }
        return false
    }

    private suspend fun isExecuteTakeProfit(
        marketId: String,
        bidPrice: Double?,
        takeProfit: Int
    ): Boolean {
        Timber.e("isExecuteTakeProfit")
        val currentTradePrice = MainViewModel.tickersMap.value[marketId]?.tradePrice
        if (currentTradePrice != null && bidPrice != null && currentTradePrice >= (bidPrice * (1 + (takeProfit / 100.0)))) {
            val askOrder = sellCrypto(marketId = marketId)
            return askOrder != null
        }
        return false
    }

    private suspend fun getDayCandles(marketId: String): List<Candle>? {
        return candleUseCase.reqDayCandle(
            market = marketId,
            to = "yyyy-MM-dd HH:mm:ss".formatedDate(),
            count = 2,
            convertingPriceUnit = "KRW"
        )
            .flowOn(ioDispatcher)
            .stateIn(CoroutineScope(ioDispatcher))
            .value
            .takeIf { it.isSuccess() }
            ?.successData()
    }

    private suspend fun getMyAssets(): List<Asset>? {
        return myAssetUseCase.reqMyAssets()
            .flowOn(ioDispatcher)
            .stateIn(CoroutineScope(ioDispatcher))
            .value
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
            .flowOn(ioDispatcher)
            .stateIn(CoroutineScope(ioDispatcher))
            .value
            .takeIf { it.isSuccess() }
            ?.successData()
    }

    companion object {
        val isRunningAutoTradingService: MutableStateFlow<Boolean> = MutableStateFlow(false)
    }
}