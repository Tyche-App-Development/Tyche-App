package com.example.tyche.api

import com.google.gson.annotations.SerializedName


data class UserResponse(
    val user: UserProfile
)

data class UserProfile(
    val id: Int,
    val username: String?,
    val email : String?,
    val fullName : String?,
    val nif: String?,
    val age: Int,
    @SerializedName("imageProfile") val imageProfile: String?,
    val apiKey: String?,
    val amount: Int,
)
