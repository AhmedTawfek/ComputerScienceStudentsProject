package com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel

import androidx.lifecycle.ViewModel
import com.example.computerscienceproject.core.presentation.isEmailCorrect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
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

    private fun validateConfirmPassword(confirmPassword: String){
        if (_uiState.value.password != confirmPassword){
            _uiState.update { it.copy(confirmPasswordError = "Passwords do not match") }
        }else{
            _uiState.update { it.copy(confirmPasswordError = "") }
        }
    }

    private fun validatePassword(password: String){
        if (password.isEmpty()) {
            _uiState.update {
                it.copy(passwordError = "You cannot leave the password empty.")
            }
            return
        }

        if (password.length >= 6){
            _uiState.update { it.copy(passwordError = "") }
        }else{
            _uiState.update { it.copy(passwordError = "Password is too small.") }
        }
    }

    private fun updateEmail(email: String){
        _uiState.update { it.copy(email = email) }
    }
    private fun updatePassword(password: String){
        _uiState.update { it.copy(password = password) }
    }
    private fun updateConfirmPassword(confirmPassword: String){
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun handleEvents(event: SignUpScreenEvents){
        when(event){
            is SignUpScreenEvents.EmailChanged -> {
                updateEmail(email = event.email)
            }
            is SignUpScreenEvents.PasswordChanged -> {
                updatePassword(password = event.password)
            }
            is SignUpScreenEvents.ConfirmPasswordChanged -> {
                updateConfirmPassword(confirmPassword = event.confirmPassword)
            }
            is SignUpScreenEvents.ValidateEmail -> {
                validateEmail(email = _uiState.value.email, errorMessage = event.emailError)
            }

             SignUpScreenEvents.ValidateConfirmPassword -> {
                validateConfirmPassword(confirmPassword = _uiState.value.confirmPassword)
            }
             SignUpScreenEvents.ValidatePassword -> {
                validatePassword(password = _uiState.value.password)
            }
        }
    }

}

data class SignUpUiState(
    val email: String = "",
    val emailError: String = "",
    val password : String = "",
    val passwordError: String = "",
    val confirmPassword : String = "",
    val confirmPasswordError: String = "",
    val isLoading: Boolean = false,
)

sealed class SignUpScreenEvents{
    data class EmailChanged(val email: String) : SignUpScreenEvents()
    data class PasswordChanged(val password: String) : SignUpScreenEvents()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpScreenEvents()
    data class ValidateEmail(val emailError: String) : SignUpScreenEvents()
    data object ValidatePassword : SignUpScreenEvents()
    data object ValidateConfirmPassword : SignUpScreenEvents()
}