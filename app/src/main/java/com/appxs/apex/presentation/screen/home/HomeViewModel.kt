package com.appxs.apex.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.domain.usecase.chat.CreateConversationUseCase
import com.appxs.apex.domain.usecase.chat.DeleteConversationUseCase
import com.appxs.apex.domain.usecase.chat.GetConversationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getConversations: GetConversationsUseCase,
    private val createConversation: CreateConversationUseCase,
    private val deleteConversation: DeleteConversationUseCase,
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
                        )
                    }
                    selectedId.value = _state.value.selectedConversationId
                }
        }


    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ConversationCreated -> createConversation(event.message)
            is HomeEvent.NewChatClicked -> goToNewChat()
            is HomeEvent.ConversationSelected -> selectConversation(event.conversationId)
            is HomeEvent.DeleteConversation -> deleteChat(event.conversation)
        }
    }

    private fun selectConversation(id: Long) {
        _state.update { it.copy(selectedConversationId = id) }
        selectedId.value = id
    }

    private fun createConversation(firstMessage: String) = viewModelScope.launch {
        val conversation = createConversation(title = null, firstMessage)
        selectConversation(conversation.id)
    }

    private fun goToNewChat() = viewModelScope.launch {
        selectConversation(0)
    }

    private fun deleteChat(conversation: Conversation) = viewModelScope.launch {
        deleteConversation(conversation)
        if (_state.value.selectedConversationId == conversation.id) {
            _state.update { it.copy(selectedConversationId = null) }
            selectedId.value = null
        }
    }
}
