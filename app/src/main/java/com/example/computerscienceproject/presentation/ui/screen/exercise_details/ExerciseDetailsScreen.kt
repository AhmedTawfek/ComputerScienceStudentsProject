package com.example.computerscienceproject.presentation.ui.screen.exercise_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.computerscienceproject.data.home.model.ExercisesModel
import com.example.computerscienceproject.presentation.ui.screen.common.BackButton
import com.example.computerscienceproject.presentation.ui.screen.common.SafeImage
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme

@Composable
fun ExerciseDetailsScreen(
    modifier: Modifier = Modifier,
    exerciseModel : ExercisesModel = ExercisesModel(
        id = 0,
        title = "TEST",
        description = "this is description",
        image = "",
        subCategoryName = ""
    ),
    closeScreen : () -> Unit = {}) {

    Column(modifier = modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)) {
        Box(
        ){

            SafeImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(330.dp),
                imageUrl = exerciseModel.image
            )

            BackButton(modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 22.dp)
                .align(androidx.compose.ui.Alignment.TopStart),
                background = MaterialTheme.colorScheme.background,
                iconColor = MaterialTheme.colorScheme.onBackground) {
                closeScreen()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 15.dp),
            text = exerciseModel.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            modifier = Modifier.padding(horizontal = 15.dp),
            text = exerciseModel.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun ExerciseDetailsScreenPreview() {

    ComputerScienceProjectTheme {
        ExerciseDetailsScreen()
    }
}



