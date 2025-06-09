package com.example.tyche.api


import UsdtBalanceResponse
import UserStrategiesResponse
import com.example.tyche.models.LoginRequest
import com.example.tyche.models.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface Endpoints {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("profile")
    fun getUser(@Header("Authorization") token: String): Call<UserResponse>

    @POST("register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @GET("balance")
    suspend fun getBalance(@Header("Authorization") token: String): BalanceResponse

    @GET("balanceusdt")
    suspend fun getBalanceUSDT(@Header("Authorization") token: String): UsdtBalanceResponse

    @GET("profitpnl")
    suspend fun getProfitPNL(@Header("Authorization") token: String): ProfitPNLResponse


    @GET("historytrade")
    suspend fun getHistoryTrade(@Header("Authorization") token: String): TradeHistoryResponse

    @POST("user-strategy")
    suspend fun sendStrategy(
        @Header("Authorization") token: String,
        @Body strategy: Strategy
    ): Response<Void>

    @GET("strategy/info")
    suspend fun getUserStrategies(
        @Header("Authorization") token: String
    ): UserStrategiesResponse




    @PUT("profile")
    fun editUserProfile(@Header("Authorization") token: String, @Body request: EditProfileRequest): Call<EditProfileResponse>


}
