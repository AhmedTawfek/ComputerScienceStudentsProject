package com.example.computerscienceproject.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = brand,
    secondary = brand2,
    onPrimary = white2,
    background = black2,
    onBackground = white,
    surfaceContainer = darkGrey,
    onSurface = lightGrey3, // For textC on textFields
    onSurfaceVariant = lightGrey2, // For icons on textFields
    outline = darkGrey2, // Border
    error = red,
)

@Composable
fun ComputerScienceProjectTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}