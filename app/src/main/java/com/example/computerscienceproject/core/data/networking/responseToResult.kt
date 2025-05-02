package com.example.computerscienceproject.core.data.networking

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.computerscienceproject.core.domain.utils.NetworkError
import retrofit2.Response

fun <T> responseToResult(response: Response<T>): Result<T, NetworkError> {
    //Log.d("Home", "responseToResult: $response")
    return when (response.code()) {
        in 200..299 -> {
            return Result.Success(response.body()!!)
        }
        400 -> {
            val errorBodyString = response.errorBody()?.string()

            val errorResponse : ApiResponse<Any> = try {
                val type = object : TypeToken<ApiResponse<T>>() {}.type
                Gson().fromJson(errorBodyString, type)
            } catch (e: Exception) {
                Log.e("Home", "Error parsing response: $errorBodyString", e)
                return Result.Error(NetworkError.UNKNOWN)
            }

            Log.d("Home", "Bad Request: $errorResponse")
            return Result.Error(NetworkError.BadRequest(errorResponse))
        }
        401 -> Result.Error(NetworkError.UNAUTHORIZED)
        408 -> Result.Error(NetworkError.RequestTimeout)
        429 -> Result.Error(NetworkError.TooManyRequests)
        in 500..599 -> {
            Log.d("Home", "safeApiCall: ${response.errorBody()}")
            Result.Error(NetworkError.ServerError)
        }
        else -> Result.Error(NetworkError.UNKNOWN)
    }
}