package com.example.computerscienceproject.presentation.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.computerscienceproject.R
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Greeting Section
        Text(
            modifier = Modifier.padding(top = 30.dp),
            text = "Hello, Ahmed 👋",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calories and Water Section
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            InfoCard(
                modifier = Modifier.weight(1.5f),
                icon = R.drawable.calories_icon,
                title = "Calories",
                value = "2,400",
                valueSubtitle = "Kcal"
            )
            InfoCard(
                modifier = Modifier.weight(1f),
                icon = R.drawable.water_icon,
                title = "Water",
                value = "4",
                valueSubtitle = "Liters"
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Workout Plans Section
        Text(
            text = "🏋️ Workout Plans",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        WorkoutPlanCard(
            imageRes = R.drawable.workout_image,
            title = "Push Pull Leg",
            description = "Short details about this workout plan"
        )

        Spacer(modifier = Modifier.height(16.dp))

        WorkoutPlanCard(
            imageRes = R.drawable.workout_image,
            title = "Full body",
            description = "Short details about this workout plan"
        )
    }
}

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    icon: Int,
    title: String,
    value: String,
    valueSubtitle: String,
) {
    Card(
        modifier = modifier
            .padding(4.dp),
        shape = RoundedCornerShape(15.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(13.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 32.sp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = valueSubtitle,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun WorkoutPlanCard(imageRes: Int, title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Image(
                modifier = Modifier
                    .padding(3.dp)
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Text(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp),
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {

    ComputerScienceProjectTheme {
        HomeScreen()
    }

}