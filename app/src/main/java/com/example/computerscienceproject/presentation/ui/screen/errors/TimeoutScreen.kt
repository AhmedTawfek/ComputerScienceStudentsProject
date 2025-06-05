package com.example.computerscienceproject.presentation.ui.screen.errors

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.computerscienceproject.R
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryButton
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme

@Composable
fun TimeoutScreen(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit) {

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background){

        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Something went wrong!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "We couldn't connect to the server. Please check your internet connection and try again.",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Retry") {
                onRetry()
            }
        }
    }

}

@Preview
@Composable
private fun TimeoutScreenPreview() {

    ComputerScienceProjectTheme {
        TimeoutScreen(onRetry = {})
    }
}