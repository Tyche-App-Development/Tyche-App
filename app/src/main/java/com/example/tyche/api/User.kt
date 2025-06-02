package com.example.tyche.api

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val user: UserProfile
)

data class UserProfile(
    val id: String,
    val username: String?,
    val email : String?,
    val name : String?,
    val nif: String?,
    val age: Int,
    @SerializedName("imageProfile") val imageProfile: String?,
    val apiKey: String?,
    val apiSecret: String?,
    val balance: Float,
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val name: String,
    val email: String,
    val age: Int,
    val nif: String,
    val balance: Int = 0,
    val apiKey: String,
    val apiSecret: String
)

data class RegisterResponse(
    val message: String,
    val user: BasicUser
)

data class BasicUser(
    val id: String,
    val username: String,
    val email: String
)

data class EditProfileRequest(
    val username: String,
    val password: String,
    val newPassword: String,
    val confirmPassword: String,
    val newImg: String,
    val name: String,
    val email: String,
    val nif: String,
    val apiKey: String,
    val apiSecret: String
)

data class EditProfileResponse(
    val message: String,
    val user: BasicUser
)