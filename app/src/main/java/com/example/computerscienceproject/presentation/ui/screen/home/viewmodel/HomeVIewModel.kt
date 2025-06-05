package com.example.computerscienceproject.presentation.ui.screen.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerscienceproject.core.data.networking.onSuccess
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.example.computerscienceproject.data.home.model.HomeModel
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
class HomeVIewModel @Inject constructor(
    private val homeRepo: HomeRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<ScreenEvents>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            homeRepo.getHomeData()
                .onSuccess { homeModel ->
                    _uiState.update { it.copy(homeModel = homeModel) }
                }
            homeRepo.getWorkoutPlans()
                .onSuccess { workoutPlansModel ->
                    _uiState.update { it.copy(workoutPlansModel = workoutPlansModel) }
                }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

}

data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val homeModel: HomeModel = HomeModel(),
    val workoutPlansModel: List<WorkoutPlansModel> = emptyList()
)