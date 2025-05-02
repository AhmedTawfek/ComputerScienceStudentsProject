package com.example.computerscienceproject.data.auth.repo

import com.example.computerscienceproject.core.data.networking.Result
import com.example.computerscienceproject.core.data.networking.map
import com.example.computerscienceproject.core.data.networking.safeApiCall
import com.example.computerscienceproject.core.domain.utils.NetworkError
import com.example.computerscienceproject.data.auth.model.LoginRequestBody
import com.example.computerscienceproject.data.auth.model.RegisterRequestBody
import com.example.computerscienceproject.data.auth.remote.AuthApi
import com.example.computerscienceproject.data.local.DataStoreDataSource

class AuthRepo(
    private val authApi: AuthApi,
    private val dataStoreDataSource: DataStoreDataSource
) {
    suspend fun register(name : String,email : String,
                         password : String, genderType : String,
                         age : String, weight : String, height : String,
                         activity : String): Result<String, NetworkError> {
        val registerBody = RegisterRequestBody(name, email, password, genderType, age, weight, height, activity)
        return safeApiCall { authApi.register(registerBody) }.map {
            it.data
        }
    }

    suspend fun login(email : String, password : String): Result<String, NetworkError> {
        val loginBody = LoginRequestBody(email, password)
        return safeApiCall { authApi.login(loginBody) }.map {
            it.data
        }
    }
}