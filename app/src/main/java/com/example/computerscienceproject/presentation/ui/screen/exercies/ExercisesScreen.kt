package com.example.computerscienceproject.presentation.ui.screen.exercies

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryTopBar
import com.example.computerscienceproject.presentation.ui.screen.common.SafeImage
import com.example.computerscienceproject.presentation.ui.screen.errors.TimeoutScreen
import com.example.computerscienceproject.presentation.ui.screen.exercies.viewmodel.ExercisesScreenEvents
import com.example.computerscienceproject.presentation.ui.screen.exercies.viewmodel.ExercisesUiState
import com.example.computerscienceproject.presentation.ui.screen.exercise_details.ExerciseDetailsScreen
import com.example.computerscienceproject.presentation.ui.screen.home.WorkoutPlanCard
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen(
    modifier: Modifier = Modifier,
    uiState: ExercisesUiState = ExercisesUiState(),
    onAction: (ExercisesScreenEvents) -> Unit = {},
    screenEvents: Flow<ScreenEvents> = emptyFlow(),
    onScreenEvents: (ScreenEvents) -> Unit = {},
) {

    val scope = rememberCoroutineScope()
    LaunchedEffect(true) {
        screenEvents.collect {
            onScreenEvents(it)
        }
    }

    if (uiState.initialLoading) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (uiState.initialDataError) {
        TimeoutScreen {
            onAction(ExercisesScreenEvents.Retry)
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        PrimaryTopBar(
            modifier = Modifier,
            title = uiState.currentSelectedWorkoutPlan.name,
            onScreenEvents = onScreenEvents,
            horizontalPadding = 20.dp
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .height(40.dp),
        ) {
            items(uiState.exercisesCategory.size) { index ->
                Log.d("ExercisesScreen", "index = $index")
                PrimaryCategoryButton(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    title = uiState.exercisesCategory[index].name,
                    imageUrl = uiState.exercisesCategory[index].image,
                    isSelected = uiState.exercisesCategory[index].id == uiState.currentSelectedSubCategoryId,
                    onClick = {
                        onAction(ExercisesScreenEvents.OnWorkoutCategorySelected(uiState.exercisesCategory[index].id))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val bottomSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { newState ->
                false //  Stop bottom sheet from hiding on outside press
            })
        var showExerciseDetailsBottomSheet by remember { mutableStateOf(false) }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            items(uiState.exercises.size) { index ->
                WorkoutPlanCard(
                    imageUrl = uiState.exercises[index].image,
                    title = uiState.exercises[index].title,
                    onClick = {
                        onAction(ExercisesScreenEvents.OnExerciseSelected(uiState.exercises[index]))
                        showExerciseDetailsBottomSheet = true
                    }
                )
            }
        }

        if (showExerciseDetailsBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { },
                sheetState = bottomSheetState,
                dragHandle = null
            ) {
                uiState.currentSelectedExercise?.let {
                    ExerciseDetailsScreen(exerciseModel = it,
                        closeScreen = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                showExerciseDetailsBottomSheet = false
                            }
                        })
                }
            }
        }

    }
}

@Composable
fun PrimaryCategoryButton(
    modifier: Modifier = Modifier,
    title: String = "Exercises",
    imageUrl: String = "",
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {

    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer

    Card(
        modifier = modifier.clickable(
            enabled = true,
            onClick = onClick
        ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(size = 32.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            SafeImage(modifier = Modifier
                .height(25.dp)
                .width(15.dp), imageUrl = imageUrl)
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

}

@Preview
@Composable
private fun ExercisesPreview() {

    ComputerScienceProjectTheme {
        ExercisesScreen()
    }
}