package com.example.computerscienceproject.presentation.ui.screen.chatbot.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerscienceproject.core.presentation.utils.ScreenEvents
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatBotViewModel @Inject constructor(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatbotUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<ScreenEvents>()
    val events = _events.receiveAsFlow()

    private var messageIdCounter = 0

    private val chat = generativeModel.startChat(
        history = listOf(
            content(role = "model") { text("How can i help you?") }
        )
    )

    private fun updateMessageInputText(text: String){
        _uiState.update {
            it.copy(messageInputText = text)
        }
    }

    private fun sendMessage(message: String) {
        viewModelScope.launch {
            val inputMessageTemp = _uiState.value.messageInputText

            messageIdCounter++
            _uiState.update {
                it.copy(
                    messages = it.messages + MessageModel(id = messageIdCounter,message = message, senderIsMe = true),
                    messageInputText = ""
                )
            }

            delay(500)

            _uiState.update {
                it.copy(isLoading = true)
            }

            try {
                val response = chat.sendMessage(
                    content(role = "user") { text(message) }
                )

                val modelResponse = response.text?.trimEnd() ?: "Sorry, I didn't understand that."

                messageIdCounter++
                _uiState.update {
                    it.copy(
                        messages = it.messages + MessageModel(id = messageIdCounter,modelResponse, senderIsMe = false),
                        messageInputText = "",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("ChatBotViewModel", "Error sending message", e)
                _uiState.update {
                    it.copy(isLoading = false,
                        messageInputText = inputMessageTemp)
                }
                _events.send(ScreenEvents.ShowSnackbar("Error sending message"))
            }
        }
    }


    fun handleEvents(event: ChatBotScreenEvents){
        when(event){
            is ChatBotScreenEvents.OnSendMessage -> {
                sendMessage(event.message)
            }
            is ChatBotScreenEvents.OnTextChange -> {
                updateMessageInputText(event.text)
            }
        }
    }
}

data class ChatbotUiState(
    val messages: List<MessageModel> = emptyList(),
    val messageInputText : String = "",
    val isLoading: Boolean = false
)

data class MessageModel(
    val id : Int,
    val message: String,
    val senderIsMe: Boolean
)

sealed class ChatBotScreenEvents {
    data class OnSendMessage(val message: String) : ChatBotScreenEvents()
    data class OnTextChange(val text: String) : ChatBotScreenEvents()
}