package com.example.computerscienceproject.presentation.ui.screen.calories_scanner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.example.computerscienceproject.core.presentation.utils.getBitmapFromUri
import com.example.computerscienceproject.presentation.ui.screen.calories_scanner.viewmodel.CaloriesScannerScreenEvents
import com.example.computerscienceproject.presentation.ui.screen.calories_scanner.viewmodel.CaloriesScannerUiState
import com.example.computerscienceproject.presentation.ui.screen.common.BackButton
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryButton
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryTopBar
import com.example.computerscienceproject.presentation.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun CaloriesScannerScreen(
    modifier: Modifier = Modifier,
    uiState: CaloriesScannerUiState = CaloriesScannerUiState(),
    onAction: (CaloriesScannerScreenEvents) -> Unit = {},
    screenEvents: Flow<ScreenEvents> = emptyFlow(),
    onScreenEvents: (ScreenEvents) -> Unit = {},
) {

    val context = LocalContext.current
    val density = LocalDensity.current
    LaunchedEffect(true) {
        screenEvents.collect {
            onScreenEvents(it)
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            Log.d("CaloriesScannerScreen", "Selected image URI: $uri")
            if (uri != null){
                val bitmap = getBitmapFromUri(context, uri)
                if (bitmap != null) {
                    onAction(CaloriesScannerScreenEvents.ProcessSelectedImage(bitmap))
                }
            }
        }
    )

    var topBarHeight by remember {
       mutableStateOf(0.dp)
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        PrimaryTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .onGloballyPositioned { layoutCoordinates ->
                    with(density) {
                        topBarHeight = layoutCoordinates.size.height.toDp()
                    }
                },
            onScreenEvents = onScreenEvents)


        Column(modifier = Modifier
            .align(Alignment.TopStart)
            .padding(vertical = topBarHeight + 50.dp)) {
            
            if (uiState.imageBitmap != null) {
                Image(
                    modifier = Modifier
                        .padding(horizontal = 60.dp)
                        .height(250.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    bitmap = uiState.imageBitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }else{
                Spacer(modifier = Modifier.size(250.dp))
            }
            
            Spacer(modifier = Modifier.height(30.dp))

            NutritionCard(
                modifier = Modifier,
                calories = uiState.calories,
                carbs = uiState.carbs,
                fats = uiState.fats,
                proteins = uiState.proteins
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            IngredientsCard(
                ingredients = uiState.ingredients
            )
        }

        PrimaryButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 30.dp),
            text = "Select your food photo to scan",
            isLoading = uiState.isLoading,
            ) {
            if (uiState.isLoading){
                return@PrimaryButton
            }
            pickImageLauncher.launch("image/*")
        }
    }
}

@Composable
fun SegmentedCircularProgress(
    carbsCalories: Float,
    fatsCalories: Float,
    proteinsCalories: Float,
    modifier: Modifier = Modifier
) {
    val totalCalories = carbsCalories + fatsCalories + proteinsCalories

    val carbsAngle = 360f * (carbsCalories / totalCalories)
    val fatsAngle = 360f * (fatsCalories / totalCalories)
    val proteinsAngle = 360f * (proteinsCalories / totalCalories)

    Canvas(modifier = modifier.size(60.dp)) {
        var startAngle = -90f
        // Carbs circle
        drawArc(
            color = Color(0xFFC05BEF), // Green - Carbs
            startAngle = startAngle,
            sweepAngle = carbsAngle,
            useCenter = false,
            style = Stroke(width = 10f, cap = StrokeCap.Round)
        )
        startAngle += carbsAngle
        drawArc(
            color = Color(0xFF4CD964), // Amber - Fats
            startAngle = startAngle,
            sweepAngle = fatsAngle,
            useCenter = false,
            style = Stroke(width = 10f, cap = StrokeCap.Round)
        )
        startAngle += fatsAngle
        drawArc(
            color = Color(0xFF14CBCB), // Blue - Proteins
            startAngle = startAngle,
            sweepAngle = proteinsAngle,
            useCenter = false,
            style = Stroke(width = 10f, cap = StrokeCap.Round)
        )

        // Handle 0 state
        if (totalCalories == 0f) {
            drawArc(
                color = lightGrey2, // Amber - Fats
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 10f, cap = StrokeCap.Round)
            )
        }
    }
}




@Composable
fun NutritionCard(
    modifier: Modifier = Modifier,
    calories: Int = 300,
    carbs: Float, fats: Float, proteins: Float) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(darkGrey)
            .padding(vertical = 38.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(contentAlignment = Alignment.Center) {
            SegmentedCircularProgress(
                carbsCalories = 0f,      // 200 kcal
                fatsCalories = 0f,       // 180 kcal
                proteinsCalories = 0f,   // 120 kcal
                modifier = Modifier
            )
            Column(modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text("$calories", style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground))
                Text("Cal", style = MaterialTheme.typography.titleMedium.copy(fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Carbs", color = brand, style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp))
            Spacer(modifier = Modifier.height(7.dp))
            Text("${carbs} g", color = white)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Fats", color = Color(0xff4CD964), style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp))
            Spacer(modifier = Modifier.height(7.dp))
            Text("${fats} g", color = white)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Proteins", color = Color(0xFF14CBCB), style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp))
            Spacer(modifier = Modifier.height(7.dp))
            Text("${proteins} g", color = white)
        }
    }
}

@Composable
fun IngredientsCard(
    modifier: Modifier = Modifier,
    ingredients : String = "") {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = darkGrey)
    ){
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(text = "Ingredients", style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp, color = MaterialTheme.colorScheme.primary))
            Spacer(modifier = Modifier.height(11.dp))
            Text(text = ingredients, textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground))
            Spacer(modifier = Modifier.height(18.dp))
        }

    }

}



@Preview
@Composable
private fun CaloriesScannerPreview() {

    ComputerScienceProjectTheme {
        CaloriesScannerScreen()
    }

}