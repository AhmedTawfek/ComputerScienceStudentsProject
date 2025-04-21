package com.example.computerscienceproject.presentation.ui.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.computerscienceproject.R
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme
import com.example.computerscienceproject.presentation.ui.theme.lightGrey4

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    textModifier: Modifier = Modifier.padding(vertical = 16.dp),
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    isEnabled : Boolean = true,
    isLoading : Boolean = false,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor, disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(7.dp),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                trackColor = MaterialTheme.colorScheme.onPrimary, modifier = Modifier
                    .size(35.dp)
                    .padding(4.dp)
            )
        } else {
            Text(modifier = textModifier, text = text, style = textStyle, color = if (isEnabled) textColor else MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit) {

    IconButton(
        modifier = modifier
            .size(40.dp)
            .background(color = lightGrey4, shape = CircleShape)
            .clip(CircleShape),
        onClick = onClick) {
        Icon(painter = painterResource(id = R.drawable.arrow_back_icon),tint = MaterialTheme.colorScheme.onBackground, contentDescription = null)
    }

}

@Preview
@Composable
private fun PreviewBackButton() {
    ComputerScienceProjectTheme {
        BackButton {

        }
    }
}