data class UsdtBalanceResponse(
    val message: String,
    val usdt: UsdtInfo
)

data class UsdtInfo(
    val asset: String,
    val amount: Int,
    val priceUSD: Double,
    val valueUSD: Double
)