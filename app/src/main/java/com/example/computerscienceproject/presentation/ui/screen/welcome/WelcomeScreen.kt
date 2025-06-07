package com.example.computerscienceproject.presentation.ui.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.computerscienceproject.R
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryButton
import com.example.computerscienceproject.presentation.ui.screen.navigation.Login
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onScreenEvents: (ScreenEvents) -> Unit = {},) {

    Box(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .background(color = MaterialTheme.colorScheme.background)) {

        Image(
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.TopCenter)
                .padding(top = 24.dp)
                .height(215.dp)
                .width(200.dp),
            painter = painterResource(id = R.drawable.welcome_logo),
            contentDescription = "Welcome Logo"
        )


        Column(modifier = Modifier.align(androidx.compose.ui.Alignment.Center),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier,
                text = "Welcome :)",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground)

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = "Our app is designed to monitor your daily calories, water and so on ...\n" +
                        "Ready to change your life?",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        PrimaryButton(
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .padding(bottom = 38.dp, start = 20.dp, end = 20.dp),
            text = "Get Started"
        ) {
            onScreenEvents(ScreenEvents.Navigate(Login))
        }
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {

    ComputerScienceProjectTheme {
        WelcomeScreen()
    }
}