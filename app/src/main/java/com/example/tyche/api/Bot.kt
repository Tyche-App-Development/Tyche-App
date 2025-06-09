data class UserStrategiesResponse(
    val message: String,
    val count: Int,
    val strategies: List<StrategyInfo>
)

data class StrategyInfo(
    val id: Int,
    val botName: String,
    val pnl: PnlInfo,
    val balance: BalanceInfo,
    val trading: TradingInfo,
    val position: PositionInfo,
    val botConfig: BotConfigInfo,
    val createdAt: String,
    val updatedAt: String
)

data class PnlInfo(
    val value: Double,
    val percentage: Double
)

data class BalanceInfo(
    val current: Double,
    val initial: Double,
    val held: Double
)

data class TradingInfo(
    val pair: String,
    val currentPrice: Double,
    val priceChange: Double
)

data class PositionInfo(
    val inPosition: Boolean,
    val buyPrice: Double,
    val lastAction: String
)

data class BotConfigInfo(
    val risk: String,
    val description: String,
    val timeInterval: String
)
