package com.appxs.apex.presentation.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender
import com.appxs.apex.domain.usecase.ai.SendMessageToAiUseCase
import com.appxs.apex.domain.usecase.chat.GetMessagesUseCase
import com.appxs.apex.domain.usecase.chat.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessages: GetMessagesUseCase,
    private val sendMessage: SendMessageUseCase,
    private val sendMessageToAi: SendMessageToAiUseCase
): ViewModel() {

    private val conversationId = MutableStateFlow<Long?>(null)

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state

    init {
        viewModelScope.launch {
            conversationId
                .filterNotNull()
                .distinctUntilChanged()
                .flatMapLatest { id ->
                    getMessages(id)
                }
                .collect { messages ->
                    _state.update { it.copy(messages = messages) }
                }
        }
    }

    fun setConversationId(id: Long) {
        conversationId.value = id
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.MessageSent -> sendMessageTask(event.message)
        }

    }

    private fun sendMessageTask(message: String) = viewModelScope.launch {
        val conversationId = conversationId.value ?: return@launch

        // Save the message of the user first
        val userMessage = sendMessage(message, conversationId)
        _state.update { state ->
            state.copy(
                messages = state.messages + userMessage
            )
        }

        // Lock user interaction once we have response from the AI
        _state.update { it.copy(isLoading = true) }

        // Ask to AI and handle the response
        val aiResult: Result<Message> = sendMessageToAi(userMessage.text, conversationId)
        _state.update { state ->
            aiResult.fold(
                onSuccess = { aiMsg ->
                    state.copy(
                        messages = state.messages + aiMsg,
                        isLoading = false
                    )
                },
                onFailure = { err ->
                    val errorMessage = Message(
                        id = 0,
                        conversationId = conversationId,
                        text = "⚠️ ${err.message ?: "Something went wrong. Please try again."}",
                        sender = Sender.Ai,
                        timestamp = System.currentTimeMillis()
                    )

                    state.copy(
                        messages = state.messages + errorMessage,
                        isLoading = false
                    )
                }
            )
        }
    }
}
