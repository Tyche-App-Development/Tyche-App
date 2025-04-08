package com.example.tyche.api

import com.example.tyche.models.LoginRequest
import com.example.tyche.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Endpoints {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}
