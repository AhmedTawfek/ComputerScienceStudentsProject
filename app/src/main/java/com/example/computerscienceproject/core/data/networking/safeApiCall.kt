package com.example.computerscienceproject.core.data.networking

import android.util.Log
import com.example.computerscienceproject.core.domain.utils.NetworkError
import retrofit2.Response
import java.io.IOException


suspend fun <T> safeApiCall(apiCall:suspend () -> Response<T>): Result<T, NetworkError> {
    val response = try {
        apiCall()
    } catch (ioException: IOException) {
        return Result.Error(NetworkError.NoInternet)
    } catch (e: Exception){
        Log.d("Home", "safeApiCall: $e | ${e.message} ${e.cause}")
        return Result.Error(NetworkError.UNKNOWN)
    }
    Log.d("Home", "safeApiCall: $response")
    val result = responseToResult(response = response)
    Log.d("Home", "safeApiCall: $result")
    return result
}