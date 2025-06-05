package com.example.computerscienceproject.data.auth.repo

import android.util.Log
import com.example.computerscienceproject.core.data.networking.Result
import com.example.computerscienceproject.core.data.networking.map
import com.example.computerscienceproject.core.data.networking.onSuccess
import com.example.computerscienceproject.core.data.networking.safeApiCall
import com.example.computerscienceproject.core.domain.utils.NetworkError
import com.example.computerscienceproject.data.auth.model.AuthResponse
import com.example.computerscienceproject.data.auth.model.LoginRequestBody
import com.example.computerscienceproject.data.auth.model.RegisterRequestBody
import com.example.computerscienceproject.data.auth.remote.AuthApi
import com.example.computerscienceproject.data.local.DataStoreDataSource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AuthRepo(
    private val authApi: AuthApi,
    private val dataStoreDataSource: DataStoreDataSource
) {
    suspend fun register(requestBody: RegisterRequestBody): Result<AuthResponse<String>, NetworkError> {
        return safeApiCall {
            authApi.register(requestBody)
        }.map {
            it
        }
    }

    suspend fun login(requestBody: LoginRequestBody): Result<AuthResponse<String>, NetworkError> {
        val loginResult =  safeApiCall { authApi.login(requestBody) }.map {
            it
        }
        loginResult.onSuccess {
            Log.d("login", "token = ${it.data}")
            dataStoreDataSource.saveValueToDataStore(key = DataStoreDataSource.USER_TOKEN, value = it.data)
        }
        return loginResult
    }
}