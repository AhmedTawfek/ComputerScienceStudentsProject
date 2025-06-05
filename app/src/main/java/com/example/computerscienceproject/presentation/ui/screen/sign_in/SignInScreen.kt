package com.example.computerscienceproject.presentation.ui.screen.sign_in

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.computerscienceproject.R
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.example.computerscienceproject.data.local.DataStoreDataSource
import com.example.computerscienceproject.presentation.di.dataStore
import com.example.computerscienceproject.presentation.ui.screen.common.ClickableText
import com.example.computerscienceproject.presentation.ui.screen.common.HeaderTitle
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryButton
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryOutlinedTextField
import com.example.computerscienceproject.presentation.ui.screen.navigation.Home
import com.example.computerscienceproject.presentation.ui.screen.sign_in.viewmodel.SignInScreenEvents
import com.example.computerscienceproject.presentation.ui.screen.sign_in.viewmodel.SignInUiState
import com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel.SignUpScreenEvents
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    state : SignInUiState = SignInUiState(),
    onAction: (SignInScreenEvents) -> Unit = {},
    screenEvents : Flow<ScreenEvents> = emptyFlow(),
    onScreenEvents : (ScreenEvents) -> Unit = {}
) {

    val context = LocalContext.current

    LaunchedEffect(true) {
        screenEvents.collect{event ->
            onScreenEvents(event)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
            .padding(start = 16.dp, end = 16.dp),
    ) {

        HeaderTitle(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 66.dp),
            text = stringResource(id = R.string.sign_in_heading_label_title)
        )

        Column(modifier = Modifier.align(Alignment.Center)) {

            PrimaryOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.sign_in_email_label_title),
                hint = stringResource(id = R.string.sign_in_email_placeholder_title),
                text = state.email,
                error = state.emailError,
                onFocusChange = { hasFocus ->
                    if (!hasFocus && state.email.isNotEmpty()) {
                        onAction(SignInScreenEvents.ValidateEmail(emailError = context.getString(R.string.sign_up_email_not_correct_label_title)))
                    }
                },
                onValueChange = {
                    onAction(SignInScreenEvents.EmailChanged(it))
                    if (state.emailError.isNotEmpty()) {
                        onAction(SignInScreenEvents.ValidateEmail(emailError = context.getString(R.string.sign_up_email_not_correct_label_title)))
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            var showPassword by remember { mutableStateOf(false) }

            PrimaryOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.sign_in_password_label_title),
                hint = stringResource(id = R.string.sign_in_password_placeholder_title),
                trailingIcon = if (showPassword) R.drawable.visiblity_off_icon else R.drawable.visibility_on_icon,
                keyboardType = if (showPassword) KeyboardType.Text else KeyboardType.Password,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIconSize = 24.dp,
                trailingIconOnClick = {
                    showPassword = !showPassword
                },
                text = state.password,
                error = state.passwordError,
                onFocusChange = { hasFocus ->
                    if (!hasFocus && state.password.isNotEmpty()) {
                        onAction(SignInScreenEvents.ValidateEmail(emailError = context.getString(R.string.sign_up_email_not_correct_label_title)))
                    }
                },
                onValueChange = {
                    onAction(SignInScreenEvents.PasswordChanged(it))
                    if (state.passwordError.isNotEmpty()) {
                        onAction(SignInScreenEvents.ValidateEmail(emailError = context.getString(R.string.sign_up_email_not_correct_label_title)))
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = stringResource(id = R.string.sign_in_button_label_title),
                isEnabled = (state.email.isNotEmpty() && state.emailError.isEmpty()) && (state.password.isNotEmpty() && state.passwordError.isEmpty()),
                isLoading = state.isLoading) {
                onAction(SignInScreenEvents.Login)
            }
        }

        Row(modifier = Modifier.align(Alignment.BottomCenter)) {

            Text(
                text = stringResource(id = R.string.sign_in_already_have_account_label_title),
                style = MaterialTheme.typography.labelMedium)

            Spacer(modifier = Modifier.width(6.dp))

            ClickableText(
                text = stringResource(id = R.string.sign_in_already_have_account_button_title),
                textStyle = MaterialTheme.typography.labelMedium
            ) {
                onAction(SignInScreenEvents.NavigateToSignUp)
            }

            Spacer(modifier = Modifier.height(50.dp))

        }
    }
}

@Preview
@Composable
private fun SignInPreview() {
    ComputerScienceProjectTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SignInScreen()
        }
    }
}