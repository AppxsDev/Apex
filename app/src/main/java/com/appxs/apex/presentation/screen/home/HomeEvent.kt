package com.appxs.apex.presentation.screen.home

import com.appxs.apex.domain.model.Conversation

sealed interface HomeEvent {
    data object NewChatClicked : HomeEvent
    data class ConversationSelected(val id: Long) : HomeEvent
    data class DeleteConversation(val conversation: Conversation) : HomeEvent
}
