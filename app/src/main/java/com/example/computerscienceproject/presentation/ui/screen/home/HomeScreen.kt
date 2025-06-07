package com.example.computerscienceproject.presentation.ui.screen.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.example.computerscienceproject.core.presentation.utils.formatNumberWithCommas
import com.example.computerscienceproject.presentation.ui.screen.common.SafeImage
import com.example.computerscienceproject.presentation.ui.screen.errors.TimeoutScreen
import com.example.computerscienceproject.presentation.ui.screen.home.viewmodel.HomeScreenEvents
import com.example.computerscienceproject.presentation.ui.screen.home.viewmodel.HomeUiState
import com.example.computerscienceproject.presentation.ui.screen.navigation.Exercises
import com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel.SignUpScreenEvents
import com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel.SignUpUiState
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState = HomeUiState(),
    onEvent: (HomeScreenEvents) -> Unit = {},
    screenEvents: Flow<ScreenEvents> = emptyFlow(),
    onScreenEvents: (ScreenEvents) -> Unit = {}
) {
    if (state.isLoading) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (state.initialDataError) {
        TimeoutScreen {
            onEvent(HomeScreenEvents.Retry)
        }
        return
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        // Greeting Section
        Text(
            modifier = Modifier.padding(top = 30.dp),
            text = "Hello, ${state.homeModel.name} 👋",
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
                value = formatNumberWithCommas(state.homeModel.calories),
                valueSubtitle = "Kcal"
            )
            InfoCard(
                modifier = Modifier.weight(1f),
                icon = R.drawable.water_icon,
                title = "Water",
                value = state.homeModel.water,
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

        // Replacing LazyColumn with Column
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            state.workoutPlansModel.forEachIndexed { index, workoutPlan ->
                WorkoutPlanCard(
                    imageUrl = workoutPlan.image,
                    title = workoutPlan.name,
                    onClick = {
                        onScreenEvents(
                            ScreenEvents.Navigate(
                                Exercises(workoutPlan.id, workoutPlan.name)
                            )
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
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
fun WorkoutPlanCard(imageUrl: String, title: String,description : String = "",onClick : () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = true,
                onClick = onClick
            ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            SafeImage(
                imageUrl = imageUrl,
                modifier = Modifier
                    .padding(3.dp)
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )
            Text(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp),
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                color = Color.White
            )
            if (description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
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