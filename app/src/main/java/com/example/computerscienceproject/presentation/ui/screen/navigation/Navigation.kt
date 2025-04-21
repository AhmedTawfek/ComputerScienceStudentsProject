package com.example.computerscienceproject.presentation.ui.screen.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.computerscienceproject.presentation.ui.screen.sign_in.SignInScreen
import com.example.computerscienceproject.presentation.ui.screen.sign_in.viewmodel.SignInViewModel
import com.example.computerscienceproject.presentation.ui.screen.sign_up.SignUpScreen
import com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel.SignUpViewModel
import com.example.computerscienceproject.presentation.ui.screen.user_info.UserInfoScreen
import com.example.computerscienceproject.presentation.ui.screen.user_info.viewmodel.UserInfoViewModel

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    paddingValues: PaddingValues,
){

    NavHost(navController = navHostController, startDestination = UserInfo){
        composable<SignUp>{

            val signUpViewModel = hiltViewModel<SignUpViewModel>()
            val state by signUpViewModel.uiState.collectAsStateWithLifecycle()

            SignUpScreen(
                modifier = modifier.padding(paddingValues),
                state = state,
                onAction = signUpViewModel::handleEvents)
        }
        composable<Login>{
            val signInViewModel = hiltViewModel<SignInViewModel>()
            val state by signInViewModel.uiState.collectAsStateWithLifecycle()

            SignInScreen(
                modifier = modifier.padding(paddingValues),
                state = state,
                onAction = signInViewModel::handleEvents
            )
        }
        composable<UserInfo> {
            val userInfoViewModel = hiltViewModel<UserInfoViewModel>()
            val state by userInfoViewModel.uiState.collectAsStateWithLifecycle()

            UserInfoScreen(
                modifier = modifier.padding(paddingValues),
                userInfoUiState = state,
                onAction = userInfoViewModel::handleEvent
            )
        }

    }

}