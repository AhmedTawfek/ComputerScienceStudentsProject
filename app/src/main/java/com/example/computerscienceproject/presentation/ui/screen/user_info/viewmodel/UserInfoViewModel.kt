package com.example.computerscienceproject.presentation.ui.screen.user_info.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(UserInfoUiState())
    val uiState = _uiState.asStateFlow()

    private fun updatePageIndex(selectedIndex: Int){
        _uiState.update {
            it.copy(currentPageIndex = selectedIndex)
        }
    }

    private fun updateSelectedIndexInUserInfo(selectedIndex: Int){
        _uiState.update {
            it.copy(
                userInfoList = it.userInfoList.mapIndexed { index, userInfoModel ->
                    if (index == it.currentPageIndex) {
                        userInfoModel.copy(selectedIndex = selectedIndex)
                    } else {
                        userInfoModel
                    }
                }
            )
        }
    }

    private fun initializeUserInfoList(){
        val userInfoList = listOf(
            UserInfoModel(
                infoType = UserInfoType.Checkbox,
                title = "What is your Gender?",
                options = listOf("Male", "Female"),
                selectedIndex = -1),
            UserInfoModel(
                infoType = UserInfoType.WheelPicker,
                title = "What is your age?",
                options = getAgeStrings(),
                selectedIndex = 10
            ),
            UserInfoModel(
                infoType = UserInfoType.WheelPicker,
                title = "What is your weight?",
                options = getWeightsStrings(),
                selectedIndex = 20),
            UserInfoModel(
                infoType = UserInfoType.WheelPicker,
                title = "What is your height?",
                options = getHeightsStrings(),
                selectedIndex = 20
            )
        )

        _uiState.update {
            it.copy(userInfoList = userInfoList)
        }
    }

    fun getAgeStrings(): List<String> = (10..100).map { it.toString() }

    fun getWeightsStrings() : List<String> = (40..200).map { "$it kg" }

    fun getHeightsStrings() : List<String> = (150..220).map { "$it cm" }

    init {
        initializeUserInfoList()
    }

    fun handleEvent(event : UserInfoScreenEvents){
        when(event){
            is UserInfoScreenEvents.OnNextClicked -> {
                updatePageIndex(_uiState.value.currentPageIndex+1)
            }
            is UserInfoScreenEvents.OnPreviousClicked -> {
                updatePageIndex(_uiState.value.currentPageIndex-1)
            }
            is UserInfoScreenEvents.OnCheckboxClicked -> {
                updateSelectedIndexInUserInfo(event.index)
            }
            is UserInfoScreenEvents.OnWheelPickerSelected -> {
                updateSelectedIndexInUserInfo(event.index)
            }
        }
    }
}

sealed class UserInfoScreenEvents{
    object OnNextClicked : UserInfoScreenEvents()
    object OnPreviousClicked : UserInfoScreenEvents()
    data class OnCheckboxClicked(val index : Int) : UserInfoScreenEvents()
    data class OnWheelPickerSelected(val index : Int) : UserInfoScreenEvents()
}

data class UserInfoUiState(
    val currentPageIndex : Int = 0,
    val userInfoList : List<UserInfoModel> = emptyList()
)

data class UserInfoModel(
    val title : String,
    val infoType : UserInfoType,
    val selectedIndex : Int = -1,
    val options : List<String>
)

enum class UserInfoType{
    Checkbox,WheelPicker
}