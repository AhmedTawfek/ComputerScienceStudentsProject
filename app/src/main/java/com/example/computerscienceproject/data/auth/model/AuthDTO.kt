package com.example.computerscienceproject.data.auth.model

data class RegisterRequestBody(
    val name : String,
    val email : String,
    val password : String,
    val genderType : String,
    val age : String,
    val weight : String,
    val height : String,
    val activity : String
)

data class  LoginRequestBody(
    val email : String,
    val password : String
)

data class User(
    val token: String
)

data class AuthResponse<T>(
    val data: T,
    val token: String,
    val status: Boolean
)