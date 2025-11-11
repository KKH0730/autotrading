package st.seno.autotrading.server.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import st.seno.autotrading.server.service.rest_api.AutoTradingApiService

@RestController
@RequestMapping("/autotrading")
class AutoTradingController(
    private val service: AutoTradingApiService,
) {

    @PostMapping("/check/trading")
    fun isRunningAutoTrading(@RequestBody body: Map<String, String>): Map<String, Any?> {
        val userKey = body["userKey"] ?: throw IllegalArgumentException("userKey is required")
        val entity = service.isRunningAutoTrading(userKey = userKey)
        return try {
            mapOf(
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
        } catch (e: Exception) {
            e.printStackTrace()
            service.saveLog(log = e.message ?: "")
            mapOf("isRunning" to "ERROR", "message" to e.message.orEmpty())
        }
    }

    @PostMapping("/stop/trading")
    fun stopTrading(@RequestBody body: Map<String, String>): Map<String, Any?> {
        return try {
            val userKey = body["userKey"] ?: throw IllegalArgumentException("userKey is required")
            val entity = service.stopAutoTrading(userKey = userKey)
            mapOf(
                "isRunning" to entity.isAutoTradingRunning,
                "message" to "Service is stopped",
            )
        } catch (e: Exception) {
            e.printStackTrace()
            service.saveLog(log = e.message ?: "")
            mapOf(
                "isRunning" to "ERROR",
                "message" to e.message.orEmpty(),
            )
        }
    }

    @PostMapping("/start/trading")
    fun startTrading(
        @RequestBody body: Map<String, Any>
    ): Map<String, Any?> {
        return try {
            val userKey = body["userKey"] as? String ?: throw IllegalArgumentException("userKey is required")
            val marketId = body["marketId"] as? String ?: throw IllegalArgumentException("marketId is required")
            val quantityRatio = body["quantityRatio"] as? Int ?: throw IllegalArgumentException("quantityRatio is required")
            val stopLoss = body["stopLoss"] as? Int ?: throw IllegalArgumentException("stopLoss is required")
            val takeProfit = body["takeProfit"] as? Int ?: throw IllegalArgumentException("takeProfit is required")
            val correctionValue = when (val value = body["correctionValue"]) {
                is Number -> value.toFloat()
                else -> throw IllegalArgumentException("correctionValue is required and must be a number")
            }
            val startDateTime = body["startDate"] as? Long ?: throw IllegalArgumentException("startDate is required")
            val endDateTime = body["endDateTime"] as? Long ?: throw IllegalArgumentException("endDateTime is required")
            val tradingStrategy = body["tradingStrategy"] as? String ?: throw IllegalArgumentException("tradingStrategy is required")

            service.startTrading(
                userKey = userKey,
                marketId = marketId,
                quantityRatio = quantityRatio,
                stopLoss = stopLoss,
                takeProfit = takeProfit,
                correctionValue = correctionValue,
                startDateTime = startDateTime,
                endDateTime = endDateTime,
                tradingStrategy = tradingStrategy
            )

            val entity = service.isRunningAutoTrading(userKey = userKey)
            mapOf(
                "isRunning" to entity.isAutoTradingRunning,
                "message" to "Service is running",
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
        } catch (e: Exception) {
            return try {
                val userKey = body["userKey"] as? String ?: throw IllegalArgumentException("userKey is required")
                stopTrading(mapOf("userKey" to userKey))
                e.printStackTrace()
                service.saveLog(log = e.message ?: "")
                mapOf("isRunning" to "ERROR", "message" to e.message.orEmpty())
            } catch (e: Exception) {
                mapOf("isRunning" to "ERROR", "message" to e.message.orEmpty())
            }
        }
    }

    @PostMapping("/trading/history")
    fun getTradingHistory(@RequestBody body: Map<String, String>): Map<String, Any?> {
        return try {
            val userKey = body["userKey"] ?: throw IllegalArgumentException("userKey is required")
            mapOf("history" to service.getTradingHistory(userKey = userKey))
        } catch (e: Exception) {
            service.saveLog(log = e.message ?: "")
            mapOf("history" to mapOf<String, Any?>())
        }
    }
}