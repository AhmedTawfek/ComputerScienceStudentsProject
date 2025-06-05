package com.example.computerscienceproject.core.presentation.utils
import android.util.Log
import com.example.computerscienceproject.R
import com.example.computerscienceproject.core.domain.utils.NetworkError

fun NetworkError.toMessage(): ErrorMessage {
    Log.d("NetworkError", this.toString())
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