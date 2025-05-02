package com.example.computerscienceproject.presentation.ui.screen.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier) -> Unit
) {
    Surface(
        modifier = modifier,
        color = Color(0xFF212121),
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            content(Modifier.weight(1f))
        }
    }
}

@Composable
fun BottomNavigationItem(
    modifier: Modifier = Modifier,
    route: Any,
    text: String,
    icon: Int,
    isSelected: Boolean,
    onClick: (Any) -> Unit
) {
    Column(
        modifier = modifier
            .clickable { onClick(route) }
            .padding(bottom = 13.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            modifier = Modifier
                .padding(top = 13.dp)
                .size(26.dp),
            painter = painterResource(id = icon),
            contentDescription = "Navigation Icon",
            tint = Color.Unspecified
        )
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}