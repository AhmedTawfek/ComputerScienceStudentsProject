package com.example.computerscienceproject.presentation.ui.screen.sign_in.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerscienceproject.core.data.networking.onError
import com.example.computerscienceproject.core.data.networking.onSuccess
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.example.computerscienceproject.core.presentation.utils.isEmailCorrect
import com.example.computerscienceproject.core.presentation.utils.toMessage
import com.example.computerscienceproject.data.auth.repo.AuthRepo
import com.example.computerscienceproject.presentation.ui.screen.navigation.Home
import com.example.computerscienceproject.presentation.ui.screen.navigation.SignUp
import com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel.SignUpScreenEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepo: AuthRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<ScreenEvents>()
    val events = _events.receiveAsFlow()

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

    private fun validatePassword(password: String){
        if (password.isEmpty()) {
            _uiState.update {
                it.copy(passwordError = "You cannot leave the password empty.")
            }
            return
        }

        if (password.length >= 8){
            _uiState.update { it.copy(passwordError = "") }
        }else{
            _uiState.update { it.copy(passwordError = "Password is too small.") }
        }
    }

    private fun login() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {

            val result = authRepo.login(
                email = _uiState.value.email,
                password = _uiState.value.password,
            ).onSuccess { result ->
                _uiState.update { it.copy(isLoading = false) }
                _events.send(
                    ScreenEvents.Navigate(
                        Home
                    )
                )
            }.onError { error ->
                _uiState.update { it.copy(isLoading = false) }
                _events.send(ScreenEvents.ShowErrorMessage(error.toMessage()))
            }
            Log.d("Register", "$result")
        }
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

            SignInScreenEvents.Login -> {
                login()
            }

            is SignInScreenEvents.ValidatePassword -> {
                validatePassword(password = _uiState.value.password)
            }

            SignInScreenEvents.NavigateToSignUp -> {
                viewModelScope.launch {
                    _events.send(ScreenEvents.Navigate(SignUp))
                }
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
    data class ValidatePassword(val paswordError : String) : SignInScreenEvents()
    data object NavigateToSignUp : SignInScreenEvents()
    object Login : SignInScreenEvents()

}