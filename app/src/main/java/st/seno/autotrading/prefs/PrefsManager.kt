package st.seno.autotrading.prefs

import com.pixplicity.easyprefs.library.Prefs
import st.seno.autotrading.data.network.model.Order
import st.seno.autotrading.extensions.gson
import st.seno.autotrading.keyname.KeyName

object PrefsManager {
    var isCompleteRequestNotification: Boolean
        get() = Prefs.getBoolean("notification", false)
        set(isComplete) {
            Prefs.putBoolean("notification", isComplete)
        }

    var marketAll: String
        get() = Prefs.getString("marketAll", "")
        set(value) {
            Prefs.putString("marketAll", value)
        }

    var marketIdList: String
        get() = Prefs.getString("marketIdList", "")
        set(value) {
            Prefs.putString("marketIdList", value)
        }

    var bookmark: String
        get() = Prefs.getString("bookmark", "")
        set(value) {
            Prefs.putString("bookmark", value)
        }

    var riseColor: Long
        get() = Prefs.getLong("riseColor", 0xFF16A34A)
        set(value) {
            Prefs.putLong("riseColor", value)
        }

    var fallColor: Long
        get() = Prefs.getLong("fallColor", 0xFFDC2626)
        set(value) {
            Prefs.putLong("fallColor", value)
        }

    var evenColor: Long
        get() = Prefs.getLong("evenColor", 0xFF4B5563)
        set(value) {
            Prefs.putLong("evenColor", value)
        }

    var selectedColorIndex: Int
        get() = Prefs.getInt("selectedColorIndex", 0)
        set(value) {
            Prefs.putInt("selectedColorIndex", value)
        }

    object AutoTrading {
        var isRunningTradingService: Boolean
            get() = Prefs.getBoolean("runningTradingService", false)
            set(isRunning) {
                Prefs.putBoolean("runningTradingService", isRunning)
            }

        var marketId: String
            get() = Prefs.getString(KeyName.Intent.MARKET_ID, "")
            set(value) {
                Prefs.putString(KeyName.Intent.MARKET_ID, value)
            }

        var quantityRatio: Int
            get() = Prefs.getInt(KeyName.Intent.QUANTITY_RATIO, 0)
            set(value) {
                Prefs.putInt(KeyName.Intent.QUANTITY_RATIO, value)
            }

        var tradingStrategy: String
            get() = Prefs.getString(KeyName.Intent.TRADING_STRATEGY, "")
            set(value) {
                Prefs.putString(KeyName.Intent.TRADING_STRATEGY, value)
            }

        var stopLoss: Int
            get() = Prefs.getInt(KeyName.Intent.STOP_LOSS, 0)
            set(value) {
                Prefs.putInt(KeyName.Intent.STOP_LOSS, value)
            }

        var stopLossPrice: String
            get() = Prefs.getString(KeyName.Intent.STOP_LOSS_PRICE, "")
            set(value) {
                Prefs.putString(KeyName.Intent.STOP_LOSS_PRICE, value)
            }

        var takeProfit: Int
            get() = Prefs.getInt(KeyName.Intent.TAKE_PROFIT, 0)
            set(value) {
                Prefs.putInt(KeyName.Intent.TAKE_PROFIT, value)
            }

        var takeProfitPrice: String
            get() = Prefs.getString(KeyName.Intent.TAKE_PROFIT_PRICE, "")
            set(value) {
                Prefs.putString(KeyName.Intent.TAKE_PROFIT_PRICE, value)
            }

        var correctionValue: Float
            get() = Prefs.getFloat(KeyName.Intent.CORRECTION_VALUE, 0f)
            set(value) {
                Prefs.putFloat(KeyName.Intent.CORRECTION_VALUE, value)
            }

        var startDate: Long
            get() = Prefs.getLong(KeyName.Intent.START_DATE, 0L)
            set(value) {
                Prefs.putLong(KeyName.Intent.START_DATE, value)
            }

        var endDate: Long
            get() = Prefs.getLong(KeyName.Intent.END_DATE, 0L)
            set(value) {
                Prefs.putLong(KeyName.Intent.END_DATE, value)
            }

        var tradingMode: String
            get() = Prefs.getString(KeyName.Intent.CURRNET_TRADING_MODE, "")
            set(value) {
                Prefs.putString(KeyName.Intent.CURRNET_TRADING_MODE, value)
            }
    }

    object Data {
        var isSkipBid: Boolean
            get() = Prefs.getBoolean(KeyName.Data.IS_SKIP_BID, false)
            set(value) {
                Prefs.putBoolean(KeyName.Data.IS_SKIP_BID, value)
            }

        var tradePrice: Double
            get() = Prefs.getDouble(KeyName.Data.TRADE_PRICE, 0.0)
            set(value) {
                Prefs.putDouble(KeyName.Data.TRADE_PRICE, value)
            }

        var bidOrder: Order?
            get() {
                val json = Prefs.getString(KeyName.Data.BID_ORDER, "")
                return try {
                    if (json.isNotEmpty()) {
                        gson.fromJson(json, Order::class.java)
                    } else {
                        null // 기본값
                    }
                } catch (e: Exception) {
                    null
                }
            }
            set(value) {
                val json = gson.toJson(value)
                Prefs.putString(KeyName.Data.BID_ORDER, json)
            }

        var askOrder: Order?
            get() {
                val json = Prefs.getString(KeyName.Data.ASK_ORDER, "")
                return try {
                    if (json.isNotEmpty()) {
                        gson.fromJson(json, Order::class.java)
                    } else {
                        null // 기본값
                    }
                } catch (e: Exception) {
                    null
                }
            }
            set(value) {
                val json = gson.toJson(value)
                Prefs.putString(KeyName.Data.ASK_ORDER, json)
            }
    }
}