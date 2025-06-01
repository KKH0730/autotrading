package st.seno.autotrading.keyname

object KeyName {
    object Intent {
        val MARKET_ID = "marketId"
        val PRICE = "price"
        val TRADING_STRATEGY = "tradingStrategy"
        val STOP_LOSS = "stopLoss"
        val STOP_LOSS_PRICE  = "stopLossPrice"
        val TAKE_PROFIT = "takeProfit"
        val TAKE_PROFIT_PRICE  = "takeProfitPrice"
        val CORRECTION_VALUE = "correctionValue"
        val QUANTITY_RATIO = "quantityRatio"
        val START_DATE = "startDate"
        val END_DATE = "endDate"
        val CURRNET_TRADING_MODE = "currentTradingMode"
    }

    object Data {
        val BID_ORDER = "bidOrder"
        val ASK_ORDER = "askOrder"
        val IS_SKIP_BID = "isSkipBid"
        val TRADE_PRICE = "tradePrice"
    }

    object Firestore {
        const val TRADING_COLLECTION = "trading"
        const val AUTO_TRADING_DOCUMENT = "autoTrading"
    }
}
