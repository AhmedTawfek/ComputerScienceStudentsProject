package com.example.computerscienceproject.core.presentation.utils
import com.example.computerscienceproject.R
import com.example.computerscienceproject.core.domain.utils.NetworkError

fun NetworkError.toMessage(): ErrorMessage {
    return when (this) {
        NetworkError.NoInternet -> {
            ErrorMessage(messageId = R.string.network_not_available)
        }
        is NetworkError.BadRequest -> {
            ErrorMessage(
                message = this.apiResponse?.message ?: ""
            )
        }
        else -> {
            ErrorMessage(messageId = R.string.api_error)
        }
    }
}

data class ErrorMessage(
    val message: String = "",
    val messageId : Int = 0,
)