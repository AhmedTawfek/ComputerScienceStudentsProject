package com.example.computerscienceproject.presentation.ui.screen.splash

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.example.computerscienceproject.data.local.DataStoreDataSource
import com.example.computerscienceproject.presentation.di.dataStore
import com.example.computerscienceproject.presentation.ui.screen.navigation.Home
import com.example.computerscienceproject.presentation.ui.screen.navigation.Login
import com.example.computerscienceproject.presentation.ui.screen.navigation.Welcome


@Composable
fun SplashScreen(modifier: Modifier = Modifier,
                 onScreenEvents: (ScreenEvents) -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(true) {
        val token = DataStoreDataSource(context.dataStore).getValue(key = DataStoreDataSource.USER_TOKEN, default = "")
        if (token.isNotEmpty()) {
            onScreenEvents(ScreenEvents.Navigate(Home))
        }else{
            onScreenEvents(ScreenEvents.Navigate(Welcome))
        }
    }
}