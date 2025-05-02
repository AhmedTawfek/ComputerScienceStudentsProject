package com.example.computerscienceproject.core.data.networking

import com.example.computerscienceproject.core.domain.utils.Error

typealias CoreError = Error

sealed interface Result<out D,out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : CoreError>(val error : E) : Result<Nothing, E>
}

inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

inline fun <T, Error : CoreError> Result<T, Error>.onSuccess(action : (T) -> Unit) : Result<T, Error> {
    return when(this){
        is Result.Success -> {
            action(data)
            this
        }
        is Result.Error -> this
    }
}

inline fun <T,Error : CoreError> Result<T, Error>.onError(action : (Error) -> Unit) : Result<T, Error> {
    return when(this){
        is Result.Success -> this
        is Result.Error -> {
            action(error)
            this
        }
    }
}