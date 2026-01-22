package com.appxs.apex.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.domain.model.ConversationType
import com.appxs.apex.domain.usecase.chat.CreateConversationUseCase
import com.appxs.apex.domain.usecase.chat.DeleteConversationUseCase
import com.appxs.apex.domain.usecase.chat.DeleteTemporalConversationsUseCase
import com.appxs.apex.domain.usecase.chat.GetConversationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getConversations: GetConversationsUseCase,
    private val createConversation: CreateConversationUseCase,
    private val deleteConversation: DeleteConversationUseCase,
    private val deleteTemporalConversations: DeleteTemporalConversationsUseCase
) : ViewModel() {

    private val selectedId = MutableStateFlow<Long?>(null)
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private val _effects = MutableSharedFlow<HomeEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<HomeEffect> = _effects.asSharedFlow()

    init {
        viewModelScope.launch {
            deleteTemporalConversations()
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
            is HomeEvent.ChangedTemporalConversation -> _state.update { it.copy(temporal = !it.temporal) }
        }
    }

    private fun changeSelectedConversation(id: Long?) {
        _state.update { it.copy(selectedConversationId = id) }
        selectedId.value = id
    }

    private fun selectConversation(id: Long?) = viewModelScope.launch {
        changeSelectedConversation(id)
        _state.update { it.copy(temporal = false) }
        deleteTemporalConversations()
    }

    /**
     * Creates a new conversation based on the user's initial message and the current state.
     *
     * This method determines the conversation type (temporal or saved) from the current state,
     * invokes the use case to persist the new conversation, updates the selected conversation ID,
     * and triggers a side effect to initiate the AI response for the first message.
     *
     * @param firstMessage The initial message text used to start the conversation.
     */
    private fun createConversation(firstMessage: String) = viewModelScope.launch {
        val type = if (state.value.temporal) ConversationType.Temp else ConversationType.Saved
        val conversation = createConversation(title = null, firstMessage, type)
        changeSelectedConversation(conversation.id)

        _effects.tryEmit(
            HomeEffect.SendAiForFirstMessage(
                conversationId = conversation.id,
                firstMessage = firstMessage
            )
        )

    }

    private fun goToNewChat() = viewModelScope.launch {
        selectConversation(null)
    }

    private fun deleteChat(conversation: Conversation) = viewModelScope.launch {
        deleteConversation(conversation)
        if (_state.value.selectedConversationId == conversation.id) {
            _state.update { it.copy(selectedConversationId = null) }
            selectedId.value = null
        }
    }
}
