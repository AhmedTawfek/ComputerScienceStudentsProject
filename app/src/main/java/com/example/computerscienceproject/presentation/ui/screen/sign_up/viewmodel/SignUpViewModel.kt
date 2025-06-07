package com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerscienceproject.core.data.networking.onError
import com.example.computerscienceproject.core.data.networking.onSuccess
import com.example.computerscienceproject.core.presentation.utils.ErrorMessage
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.example.computerscienceproject.core.presentation.utils.isEmailCorrect
import com.example.computerscienceproject.core.presentation.utils.toMessage
import com.example.computerscienceproject.core.presentation.utils.validateFullName
import com.example.computerscienceproject.data.auth.model.RegisterRequestBody
import com.example.computerscienceproject.data.auth.repo.AuthRepo
import com.example.computerscienceproject.presentation.ui.screen.navigation.Login
import com.example.computerscienceproject.presentation.ui.screen.navigation.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerRepository: AuthRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
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

    private fun validateConfirmPassword(confirmPassword: String) {
        if (_uiState.value.password != confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Passwords do not match") }
        } else {
            _uiState.update { it.copy(confirmPasswordError = "") }
        }
    }

    private fun validatePassword(password: String) {
        if (password.isEmpty()) {
            _uiState.update {
                it.copy(passwordError = "You cannot leave the password empty.")
            }
            return
        }

        if (password.length >= 8) {
            _uiState.update { it.copy(passwordError = "") }
        } else {
            _uiState.update { it.copy(passwordError = "Password is too small.") }
        }
    }

    private fun validateName(fullName: String, errorMessage: String): Boolean {
        val isValid = validateFullName(fullName)

        if (!isValid) {
            _uiState.update {
                it.copy(nameError = errorMessage)
            }
        } else {
            _uiState.update {
                it.copy(nameError = "")
            }
        }
        return isValid
    }

    fun getAgeStrings(): List<String> = (10..100).map { it.toString() }

    fun getWeightsStrings(): List<String> = (40..200).map { "$it kg" }

    fun getHeightsStrings(): List<String> = (150..220).map { "$it cm" }

    private fun initializeUserInfoList() {
        val userInfoList = listOf(
            UserInfoModel(
                infoType = UserInfoType.Checkbox,
                title = "What is your Gender?",
                options = listOf("Male", "Female"),
                selectedIndex = -1
            ),
            UserInfoModel(
                infoType = UserInfoType.WheelPicker,
                title = "What is your age?",
                options = getAgeStrings(),
                selectedIndex = 10
            ),
            UserInfoModel(
                infoType = UserInfoType.WheelPicker,
                title = "What is your weight?",
                options = getWeightsStrings(),
                selectedIndex = 20
            ),
            UserInfoModel(
                infoType = UserInfoType.WheelPicker,
                title = "What is your height?",
                options = getHeightsStrings(),
                selectedIndex = 20
            ),
            UserInfoModel(
                infoType = UserInfoType.Checkbox,
                title = "What is your activity level?",
                options = listOf("Not Very Active", "Lightly Active", "Active", "Very Active"),
                selectedIndex = -1
            ),
            UserInfoModel(
                infoType = UserInfoType.TimePicker,
                title = "When do you usually go to sleep?",
                options = listOf(), // Options not needed for a time picker
                selectedIndex = -1
            ),
            UserInfoModel(
                infoType = UserInfoType.WheelPicker,
                title = "How many hours do you sleep?",
                options = (1..24).map { "$it hours" },
                selectedIndex = 7 // Default to 8 hours
            ),
        )
        _uiState.update {
            it.copy(userInfoList = userInfoList)
        }
    }

    private fun updatePageIndex(selectedIndex: Int) {
        _uiState.update {
            it.copy(currentPageIndex = selectedIndex)
        }
    }

    private fun updateSelectedIndexInUserInfo(selectedIndex: Int) {
        _uiState.update {
            it.copy(
                userInfoList = it.userInfoList.mapIndexed { index, userInfoModel ->
                    if (index == it.currentPageIndex) {
                        userInfoModel.copy(selectedIndex = selectedIndex)
                    } else {
                        userInfoModel
                    }
                }
            )
        }
    }

    private fun updateSelectedTime(time: String) {
        _uiState.update {
            it.copy(
                userInfoList = it.userInfoList.mapIndexed { index, userInfoModel ->
                    if (index == it.currentPageIndex) {
                        userInfoModel.copy(selectedValue = time, selectedIndex = 0)
                    } else {
                        userInfoModel
                    }
                }
            )
        }
    }

    init {
        initializeUserInfoList()
    }

    private fun register() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val selectedGender =
                _uiState.value.userInfoList[0].options[_uiState.value.userInfoList[0].selectedIndex]
            val selectedAge =
                _uiState.value.userInfoList[1].options[_uiState.value.userInfoList[1].selectedIndex]
            val selectedWeight =
                _uiState.value.userInfoList[2].options[_uiState.value.userInfoList[2].selectedIndex]
            val selectedHeight =
                _uiState.value.userInfoList[3].options[_uiState.value.userInfoList[3].selectedIndex]
            val selectedActivityLevel = _uiState.value.userInfoList[4].selectedIndex + 1
            val selectedSleepFrom =
                _uiState.value.userInfoList[5].selectedValue
            val selectedSleepTo =
                _uiState.value.userInfoList[6].options[_uiState.value.userInfoList[6].selectedIndex]


            val registerRequestBody = RegisterRequestBody(
                name = _uiState.value.name,
                email = _uiState.value.email,
                password = _uiState.value.password,
                genderType = selectedGender,
                age = selectedAge,
                weight = selectedWeight,
                height = selectedHeight,
                activity = selectedActivityLevel.toString(),
                sleepAt = selectedSleepFrom,
                sleepTo = selectedSleepTo,
                firebaseToken = "zzz"
            )

            val result = registerRepository.register(
                registerRequestBody
            ).onSuccess { result ->
                if (!result.status) {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(ScreenEvents.ShowErrorMessage(message = ErrorMessage(message = result.message)))
                    return@launch
                }

                _uiState.update { it.copy(isLoading = false) }
                _events.send(ScreenEvents.ShowSuccessMessage("Account created successfully"))
                _events.send(
                    ScreenEvents.Navigate(
                        Login,
                        popUpAllScreens = true
                    )
                )
            }.onError { error ->
                _uiState.update { it.copy(isLoading = false) }
                _events.send(ScreenEvents.ShowErrorMessage(error.toMessage()))
            }

            Log.d("Register", "$result")
        }
    }


    private fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    private fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    private fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun handleEvents(event: SignUpScreenEvents) {
        when (event) {
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

            is SignUpScreenEvents.NameChanged -> {
                updateName(name = event.name)
            }

            SignUpScreenEvents.ValidateName -> {
                validateName(fullName = _uiState.value.name, errorMessage = "Name is not correct.")
            }

            SignUpScreenEvents.SignUp -> {
                validateName(fullName = uiState.value.name, errorMessage = "Name is not correct.")
                validateEmail(email = uiState.value.email, errorMessage = "Email is not correct.")
                validatePassword(password = uiState.value.password)
                validateConfirmPassword(confirmPassword = uiState.value.confirmPassword)

                if (uiState.value.nameError.isNotEmpty() || uiState.value.emailError.isNotEmpty() || uiState.value.passwordError.isNotEmpty() || uiState.value.confirmPasswordError.isNotEmpty()) return

                viewModelScope.launch {
                    _events.send(ScreenEvents.Navigate(UserInfo))
                }
            }

            // User Info
            is SignUpScreenEvents.OnNextClicked -> {
                updatePageIndex(_uiState.value.currentPageIndex + 1)
            }

            is SignUpScreenEvents.OnPreviousClicked -> {
                updatePageIndex(_uiState.value.currentPageIndex - 1)
            }

            is SignUpScreenEvents.OnCheckboxClicked -> {
                updateSelectedIndexInUserInfo(event.index)
            }

            is SignUpScreenEvents.OnWheelPickerSelected -> {
                updateSelectedIndexInUserInfo(event.index)
            }

            SignUpScreenEvents.CompleteSignUp -> {
                register()
            }

            SignUpScreenEvents.NavigateToSignIn -> {
                viewModelScope.launch {
                    _events.send(ScreenEvents.Navigate(Login))
                }
            }

            is SignUpScreenEvents.OnTimeSelected -> {
                updateSelectedTime(event.time)
            }
        }
    }
}

data class SignUpUiState(
    val name: String = "",
    val nameError: String = "",
    val email: String = "",
    val emailError: String = "",
    val password: String = "",
    val passwordError: String = "",
    val confirmPassword: String = "",
    val confirmPasswordError: String = "",
    val userInfoList: List<UserInfoModel> = emptyList(),
    val currentPageIndex: Int = 0,
    val isLoading: Boolean = false,
    val navigateToUserInfo: Boolean = false,
)

sealed class SignUpScreenEvents {
    data class NameChanged(val name: String) : SignUpScreenEvents()
    data class EmailChanged(val email: String) : SignUpScreenEvents()
    data class PasswordChanged(val password: String) : SignUpScreenEvents()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpScreenEvents()
    data object ValidateName : SignUpScreenEvents()
    data class ValidateEmail(val emailError: String) : SignUpScreenEvents()
    data object ValidatePassword : SignUpScreenEvents()
    data object ValidateConfirmPassword : SignUpScreenEvents()
    data object SignUp : SignUpScreenEvents()
    data object CompleteSignUp : SignUpScreenEvents()
    data object NavigateToSignIn : SignUpScreenEvents()

    // User Info Screen Events
    object OnNextClicked : SignUpScreenEvents()
    object OnPreviousClicked : SignUpScreenEvents()
    data class OnCheckboxClicked(val index: Int) : SignUpScreenEvents()
    data class OnWheelPickerSelected(val index: Int) : SignUpScreenEvents()
    data class OnTimeSelected(val time: String) : SignUpScreenEvents()
}

data class UserInfoUiState(
    val currentPageIndex: Int = 0,
    val userInfoList: List<UserInfoModel> = emptyList(),
)

data class UserInfoModel(
    val title: String,
    val infoType: UserInfoType,
    val selectedIndex: Int = -1,
    val options: List<String>,
    val selectedValue: String = "",
)

enum class UserInfoType {
    Checkbox,
    WheelPicker,
    TimePicker
}