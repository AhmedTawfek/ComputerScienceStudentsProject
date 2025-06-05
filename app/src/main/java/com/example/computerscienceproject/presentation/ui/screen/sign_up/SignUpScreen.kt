package com.example.computerscienceproject.presentation.ui.screen.sign_up

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
import com.example.computerscienceproject.presentation.ui.screen.common.ClickableText
import com.example.computerscienceproject.presentation.ui.screen.common.HeaderTitle
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryButton
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryOutlinedTextField
import com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel.SignUpScreenEvents
import com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel.SignUpUiState
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

// Screen
//// - BUttons, texts
// ViewModel - StateHolder - stateMAnagement
// Data

// Presentation logic
// Bussiness logic

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    state: SignUpUiState = SignUpUiState(),
    onAction: (SignUpScreenEvents) -> Unit = {},
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
            text = stringResource(id = R.string.sign_up_heading_label_title)
        )

        Column(modifier = Modifier.align(Alignment.Center)) {

            PrimaryOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.sign_up_name_label_title),
                hint = stringResource(id = R.string.sign_up_name_placeholder_title),
                text = state.name,
                error = state.nameError,
                onFocusChange = { hasFocus ->
                    if (!hasFocus && state.name.isNotEmpty()) {
                        onAction(SignUpScreenEvents.ValidateName)
                    }
                },
                onValueChange = {
                    onAction(SignUpScreenEvents.NameChanged(it))
                    if (state.nameError.isNotEmpty()) {
                        onAction(SignUpScreenEvents.ValidateName)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.sign_up_email_label_title),
                hint = stringResource(id = R.string.sign_up_email_placeholder_title),
                text = state.email,
                error = state.emailError,
                onFocusChange = { hasFocus ->
                    if (!hasFocus && state.email.isNotEmpty()) {
                        onAction(SignUpScreenEvents.ValidateEmail(emailError = context.getString(R.string.sign_up_email_not_correct_label_title)))
                    }
                },
                onValueChange = {
                    onAction(SignUpScreenEvents.EmailChanged(it))
                    if (state.emailError.isNotEmpty()) {
                        onAction(SignUpScreenEvents.ValidateEmail(emailError = context.getString(R.string.sign_up_email_not_correct_label_title)))
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            var showPassword by remember { mutableStateOf(false) }

            PrimaryOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.sign_up_password_label_title),
                hint = stringResource(id = R.string.sign_up_password_placeholder_title),
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
                        onAction(SignUpScreenEvents.ValidatePassword)
                    }
                },
                onValueChange = {
                    onAction(SignUpScreenEvents.PasswordChanged(it))
                    if (state.passwordError.isNotEmpty()) {
                        onAction(SignUpScreenEvents.ValidatePassword)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            var showConfirmPassword by remember { mutableStateOf(false) }

            PrimaryOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.sign_up_confirm_password_label_title),
                hint = stringResource(id = R.string.sign_up_confirm_password_placeholder_title),
                trailingIcon = if (showConfirmPassword) R.drawable.visiblity_off_icon else R.drawable.visibility_on_icon,
                keyboardType = if (showConfirmPassword) KeyboardType.Text else KeyboardType.Password,
                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIconSize = 24.dp,
                trailingIconOnClick = {
                    showConfirmPassword = !showConfirmPassword
                },
                text = state.confirmPassword,
                error = state.confirmPasswordError,
                onFocusChange = { hasFocus ->
                    if (!hasFocus && state.confirmPassword.isNotEmpty()) {
                        onAction(SignUpScreenEvents.ValidateConfirmPassword)
                    }
                },
                onValueChange = {
                    onAction(SignUpScreenEvents.ConfirmPasswordChanged(it))
                    if (state.confirmPasswordError.isNotEmpty()) {
                        onAction(SignUpScreenEvents.ValidateConfirmPassword)
                    }
                }
            )

            Spacer(modifier = Modifier.height(38.dp))

            PrimaryButton(
                isEnabled = (state.email.isNotEmpty() && state.emailError.isEmpty()) && (state.password.isNotEmpty() && state.passwordError.isEmpty()) && (state.confirmPassword.isNotEmpty() && state.confirmPasswordError.isEmpty()),
                isLoading = state.isLoading,
                text = stringResource(id = R.string.sign_up_button_label_title)
            ) {
                onAction(SignUpScreenEvents.SignUp)
            }
        }

        Row(modifier = Modifier.align(Alignment.BottomCenter)) {

            Text(
                text = stringResource(id = R.string.sign_up_already_have_account_button_title),
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.width(6.dp))

            ClickableText(
                text = stringResource(id = R.string.sign_up_login_label_title),
                textStyle = MaterialTheme.typography.labelMedium
            ) {
                onAction(SignUpScreenEvents.NavigateToSignIn)
            }

            Spacer(modifier = Modifier.height(50.dp))

        }
    }
}

@Preview
@Composable
private fun SignUpPreview() {
    ComputerScienceProjectTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SignUpScreen()
        }
    }
}