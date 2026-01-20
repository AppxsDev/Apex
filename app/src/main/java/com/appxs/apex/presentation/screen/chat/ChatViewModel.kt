package com.appxs.apex.presentation.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appxs.apex.domain.model.Feedback
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender
import com.appxs.apex.domain.usecase.ai.SendMessageToAiUseCase
import com.appxs.apex.domain.usecase.chat.GetMessagesUseCase
import com.appxs.apex.domain.usecase.chat.GiveFeedbackUseCase
import com.appxs.apex.domain.usecase.chat.MarkMessageAsReadUseCase
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
    private val sendMessageToAi: SendMessageToAiUseCase,
    private val giveFeedback: GiveFeedbackUseCase,
    private val markMessageAsRead: MarkMessageAsReadUseCase
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
            is ChatEvent.GiveFeedback -> giveFeedbackTask(event.message, event.feedback)
            is ChatEvent.MessageEffectFinished -> markAsReadTask(event.messageId)
        }
    }

    private fun markAsReadTask(messageId: Long) = viewModelScope.launch {
        markMessageAsRead(messageId)
        _state.update { it.copy(isEffectRunning = false) }
    }

    private fun giveFeedbackTask(message: Message, feedback: Feedback) = viewModelScope.launch {
        val updatedMessage = message.copy(feedback = feedback)
        giveFeedback(updatedMessage)

        _state.update { currentState ->
            val updatedMessages = currentState.messages.map {
                if (it.id == updatedMessage.id) updatedMessage else it
            }
            currentState.copy(messages = updatedMessages)
        }
    }

    private fun sendMessageTask(message: String) = viewModelScope.launch {
        val currentConversationId = conversationId.value ?: return@launch

        // Lock user interaction immediately
        _state.update { it.copy(isLoading = true) }

        // Save the message of the user first
        val userMessage = sendMessage(message, currentConversationId)
        
        // Ask to AI and handle the response
        val aiResult: Result<Message> = sendMessageToAi(userMessage.text, currentConversationId)
        
        _state.update { state ->
            aiResult.fold(
                onSuccess = { aiMsg ->
                    state.copy(
                        isLoading = false,
                        isEffectRunning = true // Trigger effect for the new message
                    )
                },
                onFailure = { err ->
                    state.copy(
                        isLoading = false,
                        isEffectRunning = false
                    )
                }
            )
        }
    }
}
