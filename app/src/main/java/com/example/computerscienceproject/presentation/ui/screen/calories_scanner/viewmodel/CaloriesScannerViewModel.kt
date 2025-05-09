package com.example.computerscienceproject.presentation.ui.screen.calories_scanner.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CaloriesScannerViewModel @Inject constructor(
    //private val generativeModel: GenerativeModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(CaloriesScannerUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<ScreenEvents>()
    val events = _events.receiveAsFlow()

    fun pickImageAndAnalyze(imageBitmap: Bitmap) {
        viewModelScope.launch {
            _events.send(ScreenEvents.ShowSnackbar("Please wait while analyzing the image"))
            _uiState.update { it.copy(isLoading = true, imageBitmap = imageBitmap) }

            try {
                val prompt = """
                        [SYSTEM INSTRUCTIONS]
    You are a professional nutritionist analyzing food images.
    Follow these steps:
    1. Identify all visible ingredients (comma-separated)
    2. Estimate portion sizes
    3. Calculate nutrition facts based on standard food databases
    
    Required JSON format:
    {
        "calories": number|null,
        "carbs": number|null,
        "fats": number|null,
        "proteins": number|null,
        "ingredients": "item1, item2, item3"
    }
    
    Rules:
    - If unsure about exact values, provide estimates
    - Never omit all nutrition fields
    - Ingredients should be comma-separated
                """.trimIndent()

                val content = content {
                    image(imageBitmap)
                    text(prompt)
                }

                val generativeModel = GenerativeModel(
                    apiKey = "AIzaSyA5PANpPAU86moKW5pS0ldpiGAeEZxVj8g",
                    modelName = "gemini-1.5-flash",
                    generationConfig = generationConfig {
                        temperature = 0.1f          // Lower for more factual responses
                        topP = 0.7f                 // Balanced creativity/accuracy
                        topK = 20                   // Focus on most relevant tokens
                        //maxOutputTokens = 512       // Enough for detailed analysis
                        //responseMimeType = "application/json"  // Force structured output
                       // stopSequences = listOf("]}}") // Helps terminate JSON properly
                    },
                )

                // Count tokens before sending
//                val tokenCount = generativeModel.countTokens(content)
//                val inputTokens = tokenCount.totalTokens
//                Log.d("CaloriesScannerViewModel","Gemini: Input tokens: $inputTokens") // Or log this properly

                val response = generativeModel.generateContent(
                    content
                )

                Log.d("CaloriesScannerViewModel", "Response: ${response.text}")

                // Parse the response
                val responseText = response.text?:throw Exception("Empty response from Gemini")
                val nutritionData = parseNutritionResponse(responseText)

                _events.send(ScreenEvents.ShowSnackbar("Image analyzed successfully"))
                _uiState.update {
                    it.copy(
                        calories = nutritionData?.calories ?: 0,
                        carbs = nutritionData?.carbs ?: 0f,
                        fats = nutritionData?.fats ?: 0f,
                        proteins = nutritionData?.proteins ?: 0f,
                        ingredients = nutritionData?.ingredients ?: "",
                        imageBitmap = imageBitmap,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("CaloriesScannerViewModel", "Error analyzing image", e)
                _events.send(ScreenEvents.ShowSnackbar("Failed to analyze image: ${e.localizedMessage}"))
                _uiState.update { it.copy(isLoading = false) }
                // Optionally send an error event
                //_events.send(ScreenEvents.ShowError("Failed to analyze image: ${e.localizedMessage}"))
            }
        }
    }

    private fun parseNutritionResponse(response: String?): NutritionData? {
        if (response.isNullOrEmpty()) return null

        return try {
            // Simple parsing (in a real app, you might use a proper JSON parser)
            val calories = Regex("\"calories\":\\s*(\\d+)").find(response)?.groupValues?.get(1)?.toInt() ?: 0
            val carbs = Regex("\"carbs\":\\s*(\\d+\\.?\\d*)").find(response)?.groupValues?.get(1)?.toFloat() ?: 0f
            val fats = Regex("\"fats\":\\s*(\\d+\\.?\\d*)").find(response)?.groupValues?.get(1)?.toFloat() ?: 0f
            val proteins = Regex("\"proteins\":\\s*(\\d+\\.?\\d*)").find(response)?.groupValues?.get(1)?.toFloat() ?: 0f
            val ingredients = Regex("\"ingredients\":\\s*\"([^\"]*)\"").find(response)
                ?.groupValues?.get(1) ?: ""

            NutritionData(calories, carbs, fats, proteins, ingredients)
        } catch (e: Exception) {
            null
        }
    }

    fun handleEvents(event: CaloriesScannerScreenEvents){
        when(event){
            is CaloriesScannerScreenEvents.ProcessSelectedImage -> {
                pickImageAndAnalyze(event.imageBitmap)
            }
        }
    }
}

private data class NutritionData(
    val calories: Int,
    val carbs: Float,
    val fats: Float,
    val proteins: Float,
    val ingredients: String
)

sealed class CaloriesScannerScreenEvents{
    data class ProcessSelectedImage(val imageBitmap : Bitmap) : CaloriesScannerScreenEvents()

}

data class CaloriesScannerUiState(
    val imageBitmap: Bitmap? = null,
    val calories: Int = 0,
    val carbs: Float = 0f,
    val fats: Float = 0f,
    val proteins: Float = 0f,
    val ingredients : String = "",
    val isLoading: Boolean = false
)