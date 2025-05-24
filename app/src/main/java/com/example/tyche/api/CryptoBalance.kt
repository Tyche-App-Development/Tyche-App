package com.example.tyche.api

data class BalanceAsset(
    val asset: String,
    val amount: Double,
    val priceEUR: Double,
    val valueEUR: Double
)

data class BalanceResponse(
    val message: String,
    val balanceUSD: Double,
    val assets: List<BalanceAsset>
)

data class ProfitPNLResponse(
    val message: String,
    val pnl: List<ProfitPNLItem>
)

data class ProfitPNLItem(
    val asset: String,
    val amount: Double,
    val currentPrice: Double,
    val totalBought: Double,
    val totalSold: Double,
    val averageBuyPrice: Double,
    val effectiveCost: Double,
    val currentValue: Double,
    val profit: Double,
    val pnlPercent: Double
)




