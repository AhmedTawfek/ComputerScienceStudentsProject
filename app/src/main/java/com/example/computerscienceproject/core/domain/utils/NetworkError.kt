package com.example.computerscienceproject.core.domain.utils

import com.example.computerscienceproject.core.data.networking.ApiResponse

sealed class NetworkError : Error {
    object RequestTimeout : NetworkError()
    object TooManyRequests : NetworkError()
    object UNAUTHORIZED : NetworkError()
    object NoInternet : NetworkError()
    object ServerError : NetworkError()
    data class BadRequest(val apiResponse : ApiResponse<Any>?) : NetworkError()
    object UNKNOWN : NetworkError()
}