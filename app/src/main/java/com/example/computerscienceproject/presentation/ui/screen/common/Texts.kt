package com.example.computerscienceproject.presentation.ui.screen.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.computerscienceproject.presentation.ui.theme.lightGrey

@Composable
fun HeaderTitle(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    Text(modifier = modifier, text = text, style = textStyle, color = textColor)
}

@Composable
fun HeaderDetails(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    Text(modifier = modifier, text = text, style = textStyle, color = textColor)
}

@Composable
fun ClickableText(
    modifier: Modifier = Modifier,
    text: String = "More",
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    textColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = textStyle,
        color = textColor,
        modifier = modifier.clickable(enabled = true, onClick = onClick)
    )
}

@Composable
fun TitleOnTopOfInputField(
    modifier: Modifier = Modifier,
    title: String,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    textColor: Color = lightGrey,
    verticalPadding : Dp = 10.dp,
    horizontalPadding : Dp = 0.dp
) {
    Text(
        modifier = modifier.padding(bottom = verticalPadding, start = horizontalPadding, end = horizontalPadding),
        text = title,
        style = textStyle,
        color = textColor
    )
}