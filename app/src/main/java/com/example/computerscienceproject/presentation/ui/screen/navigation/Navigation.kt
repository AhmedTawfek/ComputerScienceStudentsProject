package com.example.computerscienceproject.presentation.ui.screen.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.computerscienceproject.core.presentation.utils.ToastMessageModel
import com.example.computerscienceproject.core.presentation.utils.handleEvent
import com.example.computerscienceproject.presentation.ui.screen.home.HomeScreen
import com.example.computerscienceproject.presentation.ui.screen.sign_in.SignInScreen
import com.example.computerscienceproject.presentation.ui.screen.sign_in.viewmodel.SignInViewModel
import com.example.computerscienceproject.presentation.ui.screen.sign_up.SignUpScreen
import com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel.SignUpViewModel
import com.example.computerscienceproject.presentation.ui.screen.user_info.UserInfoScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    paddingValues: PaddingValues,
    showBottomNavigation: (Boolean) -> Unit,
    showSnackbar: (ToastMessageModel) -> Unit
){

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val signUpViewModel = hiltViewModel<SignUpViewModel>(viewModelStoreOwner)

    NavHost(navController = navHostController, startDestination = SignUp){

        composable<SignUp>{
            showBottomNavigation(false)
            val state by signUpViewModel.uiState.collectAsStateWithLifecycle()

            SignUpScreen(
                modifier = modifier.padding(paddingValues),
                state = state,
                onAction = signUpViewModel::handleEvents,
                screenEvents = signUpViewModel.events,
                onScreenEvents = {
                    it.handleEvent(navHostController, showSnackbar)
                })
        }
        composable<Login>{
            showBottomNavigation(false)
            val signInViewModel = hiltViewModel<SignInViewModel>()
            val state by signInViewModel.uiState.collectAsStateWithLifecycle()

            SignInScreen(
                modifier = modifier.padding(paddingValues),
                state = state,
                onAction = signInViewModel::handleEvents,
                screenEvents = signInViewModel.events,
                onScreenEvents = {
                    it.handleEvent(navHostController, showSnackbar)
                }
            )
        }
        composable<UserInfo> {
            showBottomNavigation(false)
            val state by signUpViewModel.uiState.collectAsStateWithLifecycle()

            UserInfoScreen(
                modifier = modifier.padding(paddingValues),
                userInfoUiState = state,
                onAction = signUpViewModel::handleEvents,
                screenEvents = signUpViewModel.events,
                onScreenEvents = {
                    it.handleEvent(navHostController, showSnackbar)
                }
            )
        }

        composable<Home> {

            showBottomNavigation(true)
            HomeScreen(
                modifier = Modifier.padding(paddingValues)
            )
        }

    }
}