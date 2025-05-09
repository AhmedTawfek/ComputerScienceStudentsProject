package com.example.computerscienceproject.presentation.ui.screen.chatbot

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.computerscienceproject.R
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.example.computerscienceproject.presentation.ui.screen.chatbot.viewmodel.ChatBotScreenEvents
import com.example.computerscienceproject.presentation.ui.screen.chatbot.viewmodel.ChatbotUiState
import com.example.computerscienceproject.presentation.ui.screen.chatbot.viewmodel.MessageModel
import com.example.computerscienceproject.presentation.ui.screen.common.PrimaryOutlinedTextField
import com.example.computerscienceproject.presentation.ui.screen.common.textFieldColors
import com.example.computerscienceproject.presentation.ui.theme.ComputerScienceProjectTheme
import com.example.computerscienceproject.presentation.ui.theme.Space
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun ChatbotScreen(
    modifier: Modifier = Modifier,
    state: ChatbotUiState = ChatbotUiState(),
    onAction: (ChatBotScreenEvents) -> Unit = {},
    screenEvents: Flow<ScreenEvents> = emptyFlow(),
    onScreenEvents: (ScreenEvents) -> Unit = {},
) {

    var chatInputHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    LaunchedEffect(true) {
        screenEvents.collect {
            onScreenEvents(it)
        }
    }

    Box(modifier = Modifier
        .safeDrawingPadding()
        .fillMaxSize()
        .padding(vertical = 10.dp)) {
        ChatTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .zIndex(1f),
            onScreenEvents = onScreenEvents
        )

        LazyColumn(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .imePadding()
                .padding(bottom = chatInputHeight)
                ,
            reverseLayout = true
        ) {
            item {
                if (state.isLoading) {
                    LoadingBubble(modifier = Modifier)
                }
            }

            items(
                key = { it.id },
                items = state.messages.reversed()
            ) { messageModel ->
                Spacer(modifier = Modifier.height(10.dp))
                MessageBubble(
                    modifier = Modifier
                        .animateItemPlacement(animationSpec = tween(durationMillis = 300))
                        .fillMaxWidth(),
                    message = messageModel.message,
                    senderIsMe = messageModel.senderIsMe
                )
            }
        }

        ChatMessageInput(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .imePadding()
                .onGloballyPositioned { coordinates ->
                    with(density){
                        chatInputHeight = coordinates.size.height.toDp() + (10).toDp()
                    }
                },
            message = state.messageInputText,
            isLoading = state.isLoading,
            onTextChanged = {
                onAction(ChatBotScreenEvents.OnTextChange(it))
            },
            onSendClick = {
                onAction(ChatBotScreenEvents.OnSendMessage(state.messageInputText))
            }
        )
    }
}

@Composable
fun MessageBubble(
    modifier: Modifier = Modifier,
    senderIsMe: Boolean = true,
    message: String,
) {

    val cardColor = if (senderIsMe) MaterialTheme.colorScheme.primary else Color(0xFF353133)
    val shape = if (senderIsMe) RoundedCornerShape(
        topStart = 30.dp,
        topEnd = 30.dp,
        bottomStart = 30.dp,
        bottomEnd = 0.dp
    ) else RoundedCornerShape(
        topStart = 30.dp,
        topEnd = 30.dp,
        bottomStart = 0.dp,
        bottomEnd = 30.dp
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {

        if (!senderIsMe) {
            Box(modifier = Modifier) {
                Image(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 9.dp)
                        .size(33.dp),
                    painter = painterResource(id = R.drawable.chatbot_icon),
                    contentDescription = null
                )
            }
        }

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .widthIn(max = (constraints.maxWidth * 0.25).dp)
                    .align(if (senderIsMe) Alignment.CenterEnd else Alignment.CenterStart),
                shape = shape,
                colors = CardDefaults.cardColors(containerColor = cardColor)
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 12.dp),
                    textAlign = TextAlign.Start,
                    softWrap = true,
                    text = message,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp)
                )
            }
        }
    }
}

@Composable
fun LoadingBubble(modifier: Modifier = Modifier) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Box(modifier = modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier) {
                Image(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 10.dp)
                        .size(33.dp),
                    painter = painterResource(id = R.drawable.chatbot_icon),
                    contentDescription = null
                )
            }

            LottieAnimation(
                modifier = Modifier.size(65.dp),
                composition = composition,
                progress = { progress }
            )
        }
    }

}

@Composable
fun ChatMessageInput(
    modifier: Modifier = Modifier,
    message: String = "",
    isLoading: Boolean = false,
    onTextChanged: (String) -> Unit = {},
    onSendClick: () -> Unit = {},
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        PrimaryOutlinedTextField(
            modifier = modifier
                .fillMaxWidth(0.9f)
                .padding(horizontal = 10.dp),
            textModifier = Modifier.padding(vertical = 18.dp, horizontal = 14.dp),
            hint = "Send message...",
            hintTextStyle = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            textColor = MaterialTheme.colorScheme.onBackground,
            colors = textFieldColors(
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
            ),
            borderThick = 1.5.dp,
            shape = RoundedCornerShape(45.dp),
            text = message,
            onValueChange = onTextChanged
        )

        IconButton(
            modifier = Modifier
                .padding(end = 5.dp)
                .size(30.dp),
            onClick = {
                if (message.isNotBlank() && !isLoading) {
                    onSendClick()
                }
            }) {
            Icon(painter = painterResource(id = R.drawable.send_icon), contentDescription = null)
        }

    }
}

@Composable
fun ChatTopBar(
    modifier: Modifier = Modifier,
    onScreenEvents: (ScreenEvents) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .size(40.dp),
            onClick = {
                onScreenEvents(ScreenEvents.NavigateBack)
            }) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.back_icon),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = null
            )
        }

        Image(
            modifier = Modifier.padding(vertical = 10.dp).size(43.dp),
            painter = painterResource(id = R.drawable.chatbot_icon), contentDescription = null
        )

        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = "Your Chatbot Helper!",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
private fun ChatbotPreview() {
    ComputerScienceProjectTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ChatbotScreen(
                modifier = Modifier,
            )
        }

    }
}