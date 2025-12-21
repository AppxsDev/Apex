package com.appxs.apex.presentation.screen.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val sendMessage: SendMessageUseCase
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
        val newMessage = sendMessage(message, conversationId.value!!)
        _state.update {
            val updatedMessages = it.messages.toMutableList().apply {
                add(newMessage)
            }
            it.copy(messages = updatedMessages)
        }
    }
}
