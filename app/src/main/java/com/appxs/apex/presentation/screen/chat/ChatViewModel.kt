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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private var thinkingJob: Job? = null

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

    fun sendFirstMessageToAi(conversationId: Long, firstMessage: String) {
        setConversationId(conversationId)
        sendToAiOnlyTask(firstMessage, conversationId)
    }

    private fun sendToAiOnlyTask(message: String, convId: Long) = viewModelScope.launch {
        // Lock user interaction immediately
        _state.update { it.copy(isLoading = true, showThinking = false) }

        // Trigger "Thinking..." after 1 second
        thinkingJob?.cancel()
        thinkingJob = viewModelScope.launch {
            delay(1000)
            _state.update { it.copy(showThinking = true) }
        }

        // Ask AI (user message already saved by CreateConversationUseCase)
        // For the first message, history is empty
        val aiResult: Result<Message> = sendMessageToAi(message, convId, emptyList())

        thinkingJob?.cancel()
        _state.update { st ->
            aiResult.fold(
                onSuccess = {
                    st.copy(
                        isLoading = false,
                        showThinking = false,
                        isEffectRunning = true
                    )
                },
                onFailure = {
                    st.copy(
                        isLoading = false,
                        showThinking = false,
                        isEffectRunning = false
                    )
                }
            )
        }
    }

    private fun sendMessageTask(message: String) = viewModelScope.launch {
        val currentConversationId = conversationId.value ?: return@launch

        // Lock user interaction immediately
        _state.update { it.copy(isLoading = true, showThinking = false) }

        // Trigger "Thinking..." after 1 second
        thinkingJob?.cancel()
        thinkingJob = viewModelScope.launch {
            delay(1000)
            _state.update { it.copy(showThinking = true) }
        }

        // Get history before saving the new user message
        val history = _state.value.messages

        // Save the message of the user first
        val userMessage = sendMessage(message, currentConversationId)
        
        // Ask to AI and handle the response
        val aiResult: Result<Message> = sendMessageToAi(userMessage.text, currentConversationId, history)
        
        thinkingJob?.cancel()
        _state.update { state ->
            aiResult.fold(
                onSuccess = { aiMsg ->
                    state.copy(
                        isLoading = false,
                        showThinking = false,
                        isEffectRunning = true // Trigger effect for the new message
                    )
                },
                onFailure = { err ->
                    state.copy(
                        isLoading = false,
                        showThinking = false,
                        isEffectRunning = false
                    )
                }
            )
        }
    }
}
