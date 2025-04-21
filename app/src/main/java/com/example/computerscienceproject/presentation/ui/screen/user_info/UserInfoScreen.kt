package com.example.computerscienceproject.presentation.ui.screen.user_info

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.computerscienceproject.presentation.ui.screen.common.BackButton
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryButton
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryTextFieldCheckbox
import com.example.computerscienceproject.presentation.ui.screen.common.UserInfoIndicators
import com.example.computerscienceproject.presentation.ui.screen.common.wheel_picker.PrimaryWheelPicker
import com.example.computerscienceproject.presentation.ui.screen.user_info.viewmodel.UserInfoModel
import com.example.computerscienceproject.presentation.ui.screen.user_info.viewmodel.UserInfoScreenEvents
import com.example.computerscienceproject.presentation.ui.screen.user_info.viewmodel.UserInfoType
import com.example.computerscienceproject.presentation.ui.screen.user_info.viewmodel.UserInfoUiState
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme
import kotlinx.coroutines.launch


@Composable
fun UserInfoScreen(
    modifier: Modifier = Modifier,
    userInfoUiState: UserInfoUiState = UserInfoUiState(
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
            )
        )
    ),
    onAction: (UserInfoScreenEvents) -> Unit = {},
){

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.padding(vertical = 10.dp, horizontal = 24.dp)) {

        Spacer(modifier = Modifier.height(10.dp))

        UserInfoIndicators(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            count = 4,
            currentSelectedIndex = userInfoUiState.currentPageIndex
        )

        Spacer(modifier = Modifier.height(37.dp))

        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { userInfoUiState.userInfoList.size }
        )

        HorizontalPager(
            state = pagerState
        ) { currentPageIndex ->
            when (userInfoUiState.userInfoList[currentPageIndex].infoType) {

                UserInfoType.Checkbox -> {
                    UserInfoCheckbox(
                        title = userInfoUiState.userInfoList[currentPageIndex].title,
                        options = userInfoUiState.userInfoList[currentPageIndex].options,
                        selectedIndex = userInfoUiState.userInfoList[currentPageIndex].selectedIndex,
                        onCheckedChange = {
                            onAction(UserInfoScreenEvents.OnCheckboxClicked(it))
                        }
                    )
                }

                UserInfoType.WheelPicker -> {
                    UserInfoWheelPicker(
                        title = userInfoUiState.userInfoList[currentPageIndex].title,
                        options = userInfoUiState.userInfoList[currentPageIndex].options,
                        defaultSelectedIndex = userInfoUiState.userInfoList[currentPageIndex].selectedIndex,
                        onItemSelected = {
                            onAction(UserInfoScreenEvents.OnWheelPickerSelected(it))
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            if (userInfoUiState.currentPageIndex > 0) {
                BackButton {
                    onAction(UserInfoScreenEvents.OnPreviousClicked)
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(userInfoUiState.currentPageIndex - 1)
                    }
                }
                Spacer(modifier = Modifier.fillMaxWidth(0.1f))
            }

            Log.d("Checkbox", "CurrentPage = ${userInfoUiState.currentPageIndex} | SelectedIndex: ${userInfoUiState.userInfoList[userInfoUiState.currentPageIndex].selectedIndex}")
            PrimaryButton(
                text = if (userInfoUiState.currentPageIndex < userInfoUiState.userInfoList.size - 1) "Next" else "Finish",
                isEnabled = (userInfoUiState.userInfoList[userInfoUiState.currentPageIndex].selectedIndex) != -1) {
                if (userInfoUiState.currentPageIndex < userInfoUiState.userInfoList.size - 1) {
                    onAction(UserInfoScreenEvents.OnNextClicked)
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(userInfoUiState.currentPageIndex + 1)
                    }
                } else {
                    //onAction(UserInfoScreenEvents.OnFinishClicked)
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

@Preview
@Composable
private fun PreviewUserInfoScreen() {
    ComputerScienceProjectTheme {
        UserInfoScreen()
    }
}