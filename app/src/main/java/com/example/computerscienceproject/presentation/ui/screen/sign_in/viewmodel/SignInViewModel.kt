package com.example.computerscienceproject.presentation.ui.screen.sign_in.viewmodel

import androidx.lifecycle.ViewModel
import com.example.computerscienceproject.core.presentation.isEmailCorrect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    private fun validateEmail(email: String, errorMessage: String) {
        if (email.isEmpty()) {
            _uiState.update {
                it.copy(emailError = errorMessage)
            }
            return
        }

        val isEmailCorrect = isEmailCorrect(email)

        if (isEmailCorrect) {
            _uiState.update { it.copy(emailError = "") }
        } else {
            _uiState.update { it.copy(emailError = errorMessage) }
        }
    }

    private fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    private fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun handleEvents(event: SignInScreenEvents) {
        when (event) {
            is SignInScreenEvents.EmailChanged -> {
                updateEmail(email = event.email)
            }

            is SignInScreenEvents.PasswordChanged -> {
                updatePassword(password = event.password)
            }

            is SignInScreenEvents.ValidateEmail -> {
                validateEmail(email = _uiState.value.email, errorMessage = event.emailError)
            }
        }
    }
}

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val isLoading: Boolean = false,
)

sealed class SignInScreenEvents {
    data class EmailChanged(val email: String) : SignInScreenEvents()
    data class PasswordChanged(val password: String) : SignInScreenEvents()
    data class ValidateEmail(val emailError: String) : SignInScreenEvents()
}