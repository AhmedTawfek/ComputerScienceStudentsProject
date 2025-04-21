package com.example.computerscienceproject.presentation.ui.screen.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme
import com.example.computerscienceproject.presentation.ui.theme.darkerWhite


@Composable
fun PrimaryIndicator(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {

    val cardColor = if (isSelected) MaterialTheme.colorScheme.primary else darkerWhite
    val borderColor = if (isSelected) darkerWhite else MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .width(50.dp)
            .height(16.dp)
            .background(cardColor, shape = RoundedCornerShape(size = 25.dp))
            .border(BorderStroke(width = 1.dp, color = borderColor), shape = RoundedCornerShape(size = 25.dp))
    )
}

@Composable
fun UserInfoIndicators(
    modifier: Modifier = Modifier,
    count: Int = 4,
    currentSelectedIndex: Int = 0,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(count) { currentIndex ->
            PrimaryIndicator(
                modifier = Modifier.padding(horizontal = 3.dp),
                isSelected = currentIndex <= currentSelectedIndex)
        }
    }
}

@Preview
@Composable
private fun PreviewUserInfoIndicators() {

    ComputerScienceProjectTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column {
                UserInfoIndicators(
                    currentSelectedIndex = 1
                )
            }
        }
    }

}