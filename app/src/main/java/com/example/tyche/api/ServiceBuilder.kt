package com.example.tyche.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    const val BASE_URL = "http://10.0.2.2:3001/api/auth/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: Endpoints = retrofit.create(Endpoints::class.java)
}
