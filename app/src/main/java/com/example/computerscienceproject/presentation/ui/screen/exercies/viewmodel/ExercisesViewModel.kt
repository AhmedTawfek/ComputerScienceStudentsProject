package com.example.computerscienceproject.presentation.ui.screen.exercies.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerscienceproject.core.data.networking.onError
import com.example.computerscienceproject.core.data.networking.onSuccess
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.example.computerscienceproject.core.presentation.utils.toMessage
import com.example.computerscienceproject.data.home.model.ExercisesCategoryModel
import com.example.computerscienceproject.data.home.model.ExercisesModel
import com.example.computerscienceproject.data.home.model.WorkoutPlansModel
import com.example.computerscienceproject.data.home.repo.HomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val homeRepo: HomeRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExercisesUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<ScreenEvents>()
    val events = _events.receiveAsFlow()

    fun handleEvents(event: ExercisesScreenEvents) {
        when (event) {
            is ExercisesScreenEvents.OnWorkoutCategorySelected -> {
                _uiState.update {
                    it.copy(currentSelectedSubCategoryId = event.id, isLoading = true)
                }
                viewModelScope.launch {
                    getExercises()
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
            ExercisesScreenEvents.Retry -> {
                viewModelScope.launch {
                    initExercises(_uiState.value.currentSelectedWorkoutPlan.id, _uiState.value.currentSelectedWorkoutPlan.name)
                }
            }
            is ExercisesScreenEvents.OnExerciseSelected -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(currentSelectedExercise = event.exerciseModel) }
                }
            }
        }
    }

    private suspend fun getExercisesCategories() {
        homeRepo.getExercisesCategories(_uiState.value.currentSelectedWorkoutPlan.id)
            .onSuccess { result ->
                _uiState.update {
                    it.copy(exercisesCategory = result, initialDataError = false)
                }
            }
            .onError {
                if (_uiState.value.exercisesCategory.isEmpty()){
                    _uiState.update {
                        it.copy(initialDataError = true)
                    }
                }else {
                    _events.send(ScreenEvents.ShowErrorMessage(it.toMessage()))
                }
            }
    }

    private suspend fun getExercises() {
        homeRepo.getExercises(subCategoryId = _uiState.value.currentSelectedSubCategoryId)
            .onSuccess { result ->
                _uiState.update {
                    it.copy(exercises = result,initialDataError = false)
                }
            }
            .onError {
                if (_uiState.value.exercisesCategory.isEmpty()){
                    _uiState.update {
                        it.copy(initialDataError = true)
                    }
                }else {
                    _events.send(ScreenEvents.ShowErrorMessage(it.toMessage()))
                }
            }
    }

    fun initExercises(id: Int, title: String) {

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    currentSelectedWorkoutPlan = WorkoutPlansModel(id = id, name = title),
                    initialLoading = true
                )
            }

            getExercisesCategories()

            if (_uiState.value.exercisesCategory.isNotEmpty()){
                _uiState.update { it.copy(currentSelectedSubCategoryId = it.exercisesCategory.first().id) }
            }

            getExercises()
            _uiState.update { it.copy(initialLoading = false) }
        }

    }
}

sealed class ExercisesScreenEvents {
    data class OnWorkoutCategorySelected(val id: Int) : ExercisesScreenEvents()
    data class OnExerciseSelected(val exerciseModel: ExercisesModel) : ExercisesScreenEvents()
    data object Retry : ExercisesScreenEvents()
}

data class ExercisesUiState(
    val initialLoading: Boolean = false,
    val initialDataError: Boolean = false,
    val isLoading: Boolean = false,
    val exercisesCategory: List<ExercisesCategoryModel> = listOf(
    ),
    val exercises: List<ExercisesModel> = emptyList(),
    val currentSelectedWorkoutPlan: WorkoutPlansModel = WorkoutPlansModel(),
    val currentSelectedExercise : ExercisesModel? = null,
    val currentSelectedSubCategoryId: Int = 0
)