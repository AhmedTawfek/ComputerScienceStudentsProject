package com.example.computerscienceproject.presentation

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.computerscienceproject.presentation.ui.screen.common.BottomNavigationBar
import com.example.computerscienceproject.presentation.ui.screen.common.BottomNavigationItem
import com.example.computerscienceproject.presentation.ui.screen.navigation.CaloriesScanner
import com.example.computerscienceproject.presentation.ui.screen.navigation.Chatbot
import com.example.computerscienceproject.presentation.ui.screen.navigation.Home
import com.example.computerscienceproject.presentation.ui.screen.navigation.Navigation
import com.example.computerscienceproject.presentation.ui.screen.navigation.Profile
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            var showBottomBar by remember { mutableStateOf(false) }

            val screensList = listOf(
                Home,
                Chatbot,
                CaloriesScanner,
                Profile
            )

            ComputerScienceProjectTheme {
                SetStatusBarTextColor()

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize().imeNestedScroll(),
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        },

                        bottomBar = {
                            if (showBottomBar) {
                                BottomNavigationBar(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .navigationBarsPadding()
                                ) { modifier ->
                                    val currentDestination = navBackStackEntry?.destination
                                    screensList.forEach { topLevelRoute ->
                                        //val isSelected = currentDestination?.hasRoute(topLevelRoute::class) == true
                                        val isSelected = currentDestination?.hierarchy?.any {
                                            it.hasRoute(topLevelRoute::class)
                                        } == true
                                        BottomNavigationItem(
                                            modifier = modifier,
                                            route = topLevelRoute,
                                            text = topLevelRoute.name,
                                            icon = if (isSelected) topLevelRoute.selectedIcon else topLevelRoute.unSelectedIcon,
                                            isSelected = isSelected,
                                            onClick = { route ->

                                                navController.navigate(topLevelRoute) {
                                                    popUpTo(Home) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    // Restore state when reselecting a previously selected item
                                                    restoreState = true
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        ) { innerPadding ->
                        Navigation(navHostController = navController,
                            paddingValues = innerPadding,
                            showBottomNavigation = { show ->
                                showBottomBar = show
                            },
                            showSnackbar = {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        message = it.message,
                                    )
                                }
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun SetStatusBarTextColor(isDarkIcons: Boolean = false) {
    val context = LocalContext.current
    val window = (context as? Activity)?.window ?: return
    val view = LocalView.current
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

        val windowInsetsController = WindowCompat.getInsetsController(window, view)
        windowInsetsController.isAppearanceLightStatusBars = isDarkIcons

    } else
        @Suppress("DEPRECATION") window.decorView.systemUiVisibility = if (isDarkIcons) {
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            0
        }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComputerScienceProjectTheme {
        Greeting("Android")
    }
}