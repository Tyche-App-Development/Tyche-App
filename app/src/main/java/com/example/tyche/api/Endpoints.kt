package com.example.tyche.api


import com.example.tyche.models.LoginRequest
import com.example.tyche.models.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Endpoints {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("profile")
    fun getUser(@Header("Authorization") token: String): Call<UserResponse>

    @POST("register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @GET("balance")
    suspend fun getBalance(@Header("Authorization") token: String): BalanceResponse

    @GET("profitpnl")
    suspend fun getProfitPNL(@Header("Authorization") token: String): ProfitPNLResponse


}
