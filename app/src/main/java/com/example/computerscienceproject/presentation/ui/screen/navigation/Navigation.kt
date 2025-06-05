package com.example.computerscienceproject.presentation.ui.screen.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.example.computerscienceproject.core.presentation.utils.ToastMessageModel
import com.example.computerscienceproject.core.presentation.utils.handleEvent
import com.example.computerscienceproject.data.local.DataStoreDataSource
import com.example.computerscienceproject.presentation.di.dataStore
import com.example.computerscienceproject.presentation.ui.screen.calories_scanner.CaloriesScannerScreen
import com.example.computerscienceproject.presentation.ui.screen.calories_scanner.viewmodel.CaloriesScannerViewModel
import com.example.computerscienceproject.presentation.ui.screen.chatbot.ChatbotScreen
import com.example.computerscienceproject.presentation.ui.screen.chatbot.viewmodel.ChatBotViewModel
import com.example.computerscienceproject.presentation.ui.screen.exercies.ExercisesScreen
import com.example.computerscienceproject.presentation.ui.screen.exercies.viewmodel.ExercisesViewModel
import com.example.computerscienceproject.presentation.ui.screen.home.HomeScreen
import com.example.computerscienceproject.presentation.ui.screen.home.viewmodel.HomeVIewModel
import com.example.computerscienceproject.presentation.ui.screen.sign_in.SignInScreen
import com.example.computerscienceproject.presentation.ui.screen.sign_in.viewmodel.SignInViewModel
import com.example.computerscienceproject.presentation.ui.screen.sign_up.SignUpScreen
import com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel.SignUpViewModel
import com.example.computerscienceproject.presentation.ui.screen.splash.SplashScreen
import com.example.computerscienceproject.presentation.ui.screen.user_info.UserInfoScreen
import com.example.computerscienceproject.presentation.ui.screen.welcome.WelcomeScreen
import javax.inject.Inject

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

    NavHost(navController = navHostController, startDestination = Splash){

        composable<Splash> {
            showBottomNavigation(false)
            SplashScreen(
                modifier = modifier.padding(paddingValues),
                onScreenEvents = {
                    it.handleEvent(navHostController, showSnackbar)
                }
            )
        }

        composable<Welcome> {
            showBottomNavigation(false)
            WelcomeScreen(
                modifier = modifier.padding(paddingValues),
                onScreenEvents = {
                    it.handleEvent(navHostController, showSnackbar)
                }
            )
        }

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

            val homeViewModel = hiltViewModel<HomeVIewModel>(
                viewModelStoreOwner = viewModelStoreOwner
            )

            val state by homeViewModel.uiState.collectAsStateWithLifecycle()

            showBottomNavigation(true)
            HomeScreen(
                modifier = Modifier.padding(paddingValues),
                state = state,
                screenEvents = homeViewModel.events,
                onEvent = homeViewModel::handleEvents,
                onScreenEvents = {
                    it.handleEvent(navHostController, showSnackbar)
                },
            )
        }

        composable<Exercises> { navBackStack ->
            val id = navBackStack.arguments?.getInt("id") ?: 1
            val title = navBackStack.arguments?.getString("title") ?: "Exercises"
            Log.d("Navigation", "Exercises: $id | title = $title")
            showBottomNavigation(false)

            val exercisesViewModel = hiltViewModel<ExercisesViewModel>(
                viewModelStoreOwner = viewModelStoreOwner
            )

            val state by exercisesViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(true) {
                exercisesViewModel.initExercises(id = id,title = title)
            }

            ExercisesScreen(
                modifier = Modifier.padding(paddingValues),
                uiState = state,
                onAction = exercisesViewModel::handleEvents,
                screenEvents = exercisesViewModel.events,
                onScreenEvents = {
                    it.handleEvent(navHostController, showSnackbar)
                }
            )
        }

        composable<Chatbot>{
            showBottomNavigation(false)

            val chatBotViewModel = hiltViewModel<ChatBotViewModel>(
                viewModelStoreOwner = viewModelStoreOwner
            )

            val state by chatBotViewModel.uiState.collectAsStateWithLifecycle()

            ChatbotScreen(
                modifier = Modifier.padding(paddingValues),
                state = state,
                onAction = chatBotViewModel::handleEvents,
                screenEvents = chatBotViewModel.events,
                onScreenEvents = {
                    it.handleEvent(navHostController, showSnackbar)
                }
            )
        }

        composable<CaloriesScanner>{
            showBottomNavigation(false)

            val caloriesScannerViewModel = hiltViewModel<CaloriesScannerViewModel>(
                viewModelStoreOwner = viewModelStoreOwner
            )

            val state by caloriesScannerViewModel.uiState.collectAsStateWithLifecycle()

            CaloriesScannerScreen(
                modifier = Modifier.padding(paddingValues),
                uiState = state,
                onAction = caloriesScannerViewModel::handleEvents,
                screenEvents = caloriesScannerViewModel.events,
                onScreenEvents = {
                    it.handleEvent(navHostController, showSnackbar)
                }
            )
        }
    }
}