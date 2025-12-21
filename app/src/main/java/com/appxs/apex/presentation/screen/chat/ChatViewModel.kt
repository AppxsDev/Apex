package com.appxs.apex.presentation.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appxs.apex.domain.usecase.chat.GetMessagesUseCase
import com.appxs.apex.domain.usecase.chat.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessages: GetMessagesUseCase,
    private val sendMessage: SendMessageUseCase
): ViewModel() {

    private val conversationId = 0L

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state

    init {
        viewModelScope.launch {
            getMessages(conversationId)
                .collect { messages ->
                    _state.update { it.copy(messages = messages) }
                }
        }
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.MessageSent -> sendMessageTask(event.message)
        }

    }

    private fun sendMessageTask(message: String) = viewModelScope.launch {
        val newMessage = sendMessage(message, conversationId)
        _state.update {
            val updatedMessages = it.messages.toMutableList().apply {
                add(newMessage)
            }
            it.copy(messages = updatedMessages)
        }
    }

    fun setConversationId(conversationId: Long) {
        this.conversationId
    }
}
