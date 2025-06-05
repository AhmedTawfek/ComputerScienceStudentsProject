package com.example.computerscienceproject.data.auth.model
import com.google.gson.annotations.SerializedName


data class RegisterRequestBody(
    @SerializedName("name")
    val name : String,
    @SerializedName("email")
    val email : String,
    @SerializedName("password")
    val password : String,
    @SerializedName("gender_type")
    val genderType : String? = null,
    @SerializedName("age")
    val age : String? = null,
    @SerializedName("weight")
    val weight : String? = null,
    @SerializedName("height")
    val height : String? = null,
    @SerializedName("activity")
    val activity : String? = null,
    @SerializedName("sleep_at")
    val sleepAt : String? = null,
    @SerializedName("waked_at")
    val sleepTo : String? = null,
    @SerializedName("firebase_token")
    val firebaseToken : String? = null
)

data class RegisterRequestBody2(
    val email : String,
)

data class  LoginRequestBody(
    @SerializedName("email")
    val email : String,
    @SerializedName("password")
    val password : String,
    @SerializedName("firebase_token")
    val firebaseToken : String
)

data class User(
    val token: String
)

data class AuthResponse<T>(
    val data: T,
    val message: String,
    val status: Boolean
)