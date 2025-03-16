package com.example.computerscienceproject.presentation.ui.screen.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    textModifier: Modifier = Modifier.padding(vertical = 16.dp),
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        shape = RoundedCornerShape(7.dp),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick
    ) {
        Text(modifier = textModifier,text = text, style = textStyle, color = textColor)
    }
}