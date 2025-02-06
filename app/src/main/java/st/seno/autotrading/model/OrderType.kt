package st.seno.autotrading.model

enum class OrderType(val value: String) {
    LIMIT(value = "limit"), // 지정가 주문
    PRICE(value = "price"), // 시장가 주문(매수)
    MARKET(value = "market"), // 시장가 주문(매도)
    BEST(value = "best"), // 최유리 주문
}