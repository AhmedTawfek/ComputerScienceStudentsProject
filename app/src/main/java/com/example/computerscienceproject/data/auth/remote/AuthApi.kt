package com.example.computerscienceproject.data.auth.remote

import com.example.computerscienceproject.core.data.networking.ApiConstants.AUTH_PATH
import com.example.computerscienceproject.data.auth.model.AuthResponse
import com.example.computerscienceproject.data.auth.model.LoginRequestBody
import com.example.computerscienceproject.data.auth.model.RegisterRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("$AUTH_PATH/register")
    suspend fun register(
        @Body request: RegisterRequestBody
    ): Response<AuthResponse<String>>

    @POST("$AUTH_PATH/login")
    suspend fun login(
        @Body request: LoginRequestBody
    ): Response<AuthResponse<String>>

}