package com.example.tyche.api

import com.google.gson.annotations.SerializedName

data class Strategy(
    val symbol: String,
    val risk: String,
    val amount: Double
)

