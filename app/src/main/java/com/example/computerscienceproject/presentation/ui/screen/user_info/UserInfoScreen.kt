package com.example.computerscienceproject.presentation.ui.screen.user_info

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.example.computerscienceproject.presentation.ui.screen.common.BackButton
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryButton
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryTextFieldCheckbox
import com.example.computerscienceproject.presentation.ui.screen.common.UserInfoIndicators
import com.example.computerscienceproject.presentation.ui.screen.common.wheel_picker.PrimaryWheelPicker
import com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel.SignUpScreenEvents
import com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel.SignUpUiState
import com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel.UserInfoModel
import com.example.computerscienceproject.presentation.ui.screen.sign_up.viewmodel.UserInfoType
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun UserInfoScreen(
    modifier: Modifier = Modifier,
    userInfoUiState: SignUpUiState = SignUpUiState(
        currentPageIndex = 0,
        userInfoList = listOf(
            UserInfoModel(
                infoType = UserInfoType.Checkbox,
                title = "What is your Gender?",
                options = listOf("Male", "Female"),
                selectedIndex = -1),
            UserInfoModel(
                infoType = UserInfoType.WheelPicker,
                title = "What is your age?",
                options = (10..100).map { it.toString() },
                selectedIndex = 10
            ),
            UserInfoModel(
                infoType = UserInfoType.WheelPicker,
                title = "What is your weight?",
                options = (40..200).map { "$it kg" },
                selectedIndex = 20),
            UserInfoModel(
                infoType = UserInfoType.WheelPicker,
                title = "What is your height?",
                options = (150..220).map { "$it cm" },
                selectedIndex = 20
            ),
        )
    ),
    onAction: (SignUpScreenEvents) -> Unit = {},
    screenEvents : Flow<ScreenEvents> = emptyFlow(),
    onScreenEvents : (ScreenEvents) -> Unit = {}
){
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        screenEvents.collect{event ->
            onScreenEvents(event)
        }
    }

    Column(modifier = modifier.padding(vertical = 10.dp)) {

        Spacer(modifier = Modifier.height(10.dp))

        UserInfoIndicators(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            count = userInfoUiState.userInfoList.size,
            currentSelectedIndex = userInfoUiState.currentPageIndex
        )

        Spacer(modifier = Modifier.height(37.dp))

        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { userInfoUiState.userInfoList.size }
        )

        HorizontalPager(
            modifier = Modifier.padding(horizontal = 24.dp),
            userScrollEnabled = false,
            state = pagerState
        ) { currentPageIndex ->
            when (userInfoUiState.userInfoList[currentPageIndex].infoType) {

                UserInfoType.Checkbox -> {
                    UserInfoCheckbox(
                        title = userInfoUiState.userInfoList[currentPageIndex].title,
                        options = userInfoUiState.userInfoList[currentPageIndex].options,
                        selectedIndex = userInfoUiState.userInfoList[currentPageIndex].selectedIndex,
                        onCheckedChange = {
                            onAction(SignUpScreenEvents.OnCheckboxClicked(it))
                        }
                    )
                }

                UserInfoType.WheelPicker -> {
                    UserInfoWheelPicker(
                        title = userInfoUiState.userInfoList[currentPageIndex].title,
                        options = userInfoUiState.userInfoList[currentPageIndex].options,
                        defaultSelectedIndex = userInfoUiState.userInfoList[currentPageIndex].selectedIndex,
                        onItemSelected = {
                            onAction(SignUpScreenEvents.OnWheelPickerSelected(it))
                        }
                    )
                }

                UserInfoType.TimePicker -> {
                    UserInfoTimePicker(
                        title = userInfoUiState.userInfoList[currentPageIndex].title,
                        selectedTime = userInfoUiState.userInfoList[currentPageIndex].selectedValue,
                        onTimeSelected = {
                            Log.d("UserInfo","SelectedTime => $it")
                            onAction(SignUpScreenEvents.OnTimeSelected(it))
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically) {

            if (userInfoUiState.currentPageIndex > 0) {
                BackButton {
                    onAction(SignUpScreenEvents.OnPreviousClicked)
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(userInfoUiState.currentPageIndex - 1)
                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth(0.1f))
            }

            PrimaryButton(
                isLoading = userInfoUiState.isLoading,
                text = if (userInfoUiState.currentPageIndex < userInfoUiState.userInfoList.size - 1) "Next" else "Finish",
                isEnabled = (userInfoUiState.userInfoList[userInfoUiState.currentPageIndex].selectedIndex) != -1 && !userInfoUiState.isLoading) {

                if (userInfoUiState.currentPageIndex < userInfoUiState.userInfoList.size - 1) {
                    onAction(SignUpScreenEvents.OnNextClicked)
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(userInfoUiState.currentPageIndex + 1)
                    }
                } else {
                    onAction(SignUpScreenEvents.CompleteSignUp)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun UserInfoCheckbox(
    modifier: Modifier = Modifier,
    title: String,
    options: List<String>,
    selectedIndex: Int,
    onCheckedChange: (Int) -> Unit = {},
) {

    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        for (i in options.indices) {
            PrimaryTextFieldCheckbox(
                modifier = Modifier.fillMaxWidth(),
                text = options[i],
                checked = selectedIndex == i,
                onCheckedChange = {
                    Log.d("Checkbox", "UserInfoCheckbox: $it")
                    if (i != selectedIndex) {
                        onCheckedChange(i)
                    }
                })
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun UserInfoWheelPicker(
    modifier: Modifier = Modifier,
    title: String,
    options: List<String>,
    defaultSelectedIndex: Int = 1,
    onItemSelected: (Int) -> Unit = {},
) {

    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryWheelPicker(
            modifier = Modifier
                .fillMaxWidth(1f)
                .align(Alignment.CenterHorizontally),
            items = options,
            defaultSelectedIndex = defaultSelectedIndex,
            onItemSelected = {
                Log.d("Checkbox", "UserInfoWheelPicker: $it")
                onItemSelected(it) }
        )
    }
}



@Composable
fun UserInfoTimePicker(
    modifier: Modifier = Modifier,
    title: String,
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(text = selectedTime.ifEmpty { "Select Time" }) {
            showDialog = true
        }

        if (showDialog) {
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hour: Int, minute: Int ->
                    val amPm = if (hour < 12) "AM" else "PM"
                    val hour12 = when {
                        hour == 0 -> 12
                        hour > 12 -> hour - 12
                        else -> hour
                    }
                    val formatted = String.format("%02d:%02d %s", hour12, minute, amPm)
                    onTimeSelected(formatted)
                    showDialog = false
                },
                10, 0, false // <- false = 12-hour format
            )
            timePickerDialog.show()
        }
    }
}



@Preview
@Composable
private fun PreviewUserInfoScreen() {
    ComputerScienceProjectTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            UserInfoScreen()
        }
    }
}