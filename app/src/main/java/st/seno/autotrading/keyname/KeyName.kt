package st.seno.autotrading.keyname

object KeyName {
    object Intent {
        val MARKET_ID = "marketId"
        val PRICE = "price"
        val TRADING_STRATEGY = "tradingStrategy"
        val STOP_LOSS = "stopLoss"
        val TAKE_PROFIT = "takeProfit"
        val CORRECTION_VALUE = "correctionValue"
        val QUANTITY_RATIO = "quantityRatio"
        val START_DATE = "startDate"
        val END_DATE = "endDate"
        val CURRNET_TRADING_MODE = "currentTradingMode"
    }

    object Firestore {
        const val TRADING_COLLECTION = "trading"
        const val AUTO_TRADING_DOCUMENT = "autoTrading"
    }
}
