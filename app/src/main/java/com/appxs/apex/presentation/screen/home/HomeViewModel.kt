package com.appxs.apex.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.domain.usecase.chat.CreateConversationUseCase
import com.appxs.apex.domain.usecase.chat.DeleteConversationUseCase
import com.appxs.apex.domain.usecase.chat.GetConversationsUseCase
import com.appxs.apex.domain.usecase.chat.GetMessagesUseCase
import com.appxs.apex.domain.usecase.chat.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getConversations: GetConversationsUseCase,
    private val getMessages: GetMessagesUseCase,
    private val createConversation: CreateConversationUseCase,
    private val deleteConversation: DeleteConversationUseCase,
    private val sendMessage: SendMessageUseCase
) : ViewModel() {

    private val selectedId = MutableStateFlow<Long?>(null)
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        viewModelScope.launch {
            getConversations()
                .collect { conversations ->
                    _state.update { st ->
                        st.copy(
                            conversations = conversations,
                            selectedConversationId = st.selectedConversationId ?: conversations.firstOrNull()?.id
                        )
                    }
                    selectedId.value = _state.value.selectedConversationId
                }
        }

        // Messages for selected conversation (auto switches)
        viewModelScope.launch {
            selectedId
                .filterNotNull()
                .distinctUntilChanged()
                .flatMapLatest { id -> getMessages(id) }
                .collect { messages ->
                    _state.update { st -> st.copy(messages = messages) }
                }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.NewChatClicked -> createNewChat()
            is HomeEvent.ConversationSelected -> selectConversation(event.id)
            is HomeEvent.DeleteConversation -> deleteChat(event.conversation)

            is HomeEvent.InputChanged -> _state.update { it.copy(inputText = event.text) }
            HomeEvent.SendClicked -> sendCurrentMessage()
        }
    }

    private fun selectConversation(id: Long) {
        _state.update { it.copy(selectedConversationId = id, error = null) }
        selectedId.value = id
    }

    private fun createNewChat() = viewModelScope.launch {
        val id = createConversation(title = null)
        selectConversation(0)
    }

    private fun deleteChat(conversation: Conversation) = viewModelScope.launch {
        deleteConversation(conversation)
        if (_state.value.selectedConversationId == conversation.id) {
            _state.update { it.copy(selectedConversationId = null, messages = emptyList()) }
            selectedId.value = null
        }
    }

    private fun sendCurrentMessage() = viewModelScope.launch {
        val id = _state.value.selectedConversationId ?: return@launch
        val text = _state.value.inputText.trim()
        if (text.isEmpty()) return@launch

        _state.update { it.copy(inputText = "", isLoading = true, error = null) }

        runCatching { sendMessage(conversationId = id, message = text) }
            .onFailure { e -> _state.update { it.copy(error = e.message, isLoading = false) } }
            .onSuccess { _state.update { it.copy(isLoading = false) } }
    }
}
