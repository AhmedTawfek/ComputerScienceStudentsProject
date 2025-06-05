package com.example.computerscienceproject.data.auth.remote
import com.example.computerscienceproject.data.auth.model.AuthResponse
import com.example.computerscienceproject.data.auth.model.LoginRequestBody
import com.example.computerscienceproject.data.auth.model.RegisterRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthApi {

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequestBody
    ): Response<AuthResponse<String>>

    @POST("login")
    suspend fun login(
        @Body request: LoginRequestBody
    ): Response<AuthResponse<String>>

}