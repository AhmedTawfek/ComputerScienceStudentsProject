package com.example.computerscienceproject.core.presentation.utils

import androidx.navigation.NavHostController
import com.example.computerscienceproject.presentation.ui.screen.navigation.Home
import com.example.computerscienceproject.presentation.ui.screen.navigation.Login

sealed class ScreenEvents {
    data class Navigate(val screenToNavigate: Any) : ScreenEvents()
    data class NavigateWithArgs(
        val screenToNavigate: Any,
        val argsKey: String,
        val argsValue: Any
    ) : ScreenEvents()

    object NavigateBack : ScreenEvents()
    object Logout : ScreenEvents()
    data class ShowSnackbar(val message: String) : ScreenEvents()
    data class ShowErrorMessage(val message: ErrorMessage) : ScreenEvents()
    data class ShowSuccessMessage(val message: String) : ScreenEvents()
}


fun ScreenEvents.handleEvent(
    navHostController: NavHostController,
    showSnackbar: (ToastMessageModel) -> Unit
) {
    when (this) {
        is ScreenEvents.Navigate -> {
            navHostController.navigate(this.screenToNavigate) {
                launchSingleTop = true
                if (this@handleEvent.screenToNavigate == Home) {
                    popUpTo(0)
                }
            }
        }

        is ScreenEvents.ShowErrorMessage -> {
            showSnackbar(
                ToastMessageModel(
                    this.message.messageId,
                    this.message.message,
                    isSuccess = false
                )
            )
        }

        is ScreenEvents.ShowSuccessMessage -> {
            showSnackbar(ToastMessageModel(message = this.message, isSuccess = true))
        }

        ScreenEvents.NavigateBack -> {
            navHostController.popBackStack()
        }

        ScreenEvents.Logout -> {
            //DataStoreDataSource.USER_LOGGED_IN = false
            navHostController.navigate(
                Login
            ) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        }

        is ScreenEvents.NavigateWithArgs -> {
            navHostController.currentBackStackEntry?.savedStateHandle?.set(key = this.argsKey, value = this.argsValue)
            navHostController.navigate(this.screenToNavigate) {
                popUpTo(Home) {
                    saveState = true
                }
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }

        is ScreenEvents.ShowSnackbar -> {
            showSnackbar(ToastMessageModel(message = this.message))
        }
    }
}

data class ToastMessageModel(
    val messageResource: Int = 0,
    val message: String = "",
    val isSuccess: Boolean = false
)