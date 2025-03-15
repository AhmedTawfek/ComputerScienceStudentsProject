package com.example.computerscienceproject.presentation.ui.screen.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun textFieldColors(
    focusedContainerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    unfocusedContainerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.outline,
    unfocusedTextColor: Color = MaterialTheme.colorScheme.onSurface,
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    focusedTextColor: Color = MaterialTheme.colorScheme.onSurface,
    unfocusedLeadingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedLeadingIconColor: Color = MaterialTheme.colorScheme.primary, // assuming Brand is Color.Blue for simplicity
    unfocusedTrailingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedTrailingIconColor: Color = MaterialTheme.colorScheme.primary,
    errorBorderColor: Color = MaterialTheme.colorScheme.error,
    errorCursorColor: Color = MaterialTheme.colorScheme.error,
    errorTextColor: Color = MaterialTheme.colorScheme.onSurface,
) =
    OutlinedTextFieldDefaults.colors(
        focusedContainerColor = focusedContainerColor,
        unfocusedContainerColor = unfocusedContainerColor,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        unfocusedTextColor = unfocusedTextColor,
        cursorColor = cursorColor,
        focusedTextColor = focusedTextColor,
        unfocusedLeadingIconColor = unfocusedLeadingIconColor,
        focusedLeadingIconColor = focusedLeadingIconColor,
        unfocusedTrailingIconColor = unfocusedTrailingIconColor,
        focusedTrailingIconColor = focusedTrailingIconColor,
        errorBorderColor = errorBorderColor,
        errorContainerColor = errorCursorColor,
        errorTextColor = errorTextColor,
    )