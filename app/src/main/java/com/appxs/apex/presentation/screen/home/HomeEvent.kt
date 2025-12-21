package com.appxs.apex.presentation.screen.home

import com.appxs.apex.domain.model.Conversation

sealed interface HomeEvent {
    data object NewChatClicked : HomeEvent
    data class ConversationCreated(val message: String) : HomeEvent
    data class ConversationSelected(val conversationId: Long) : HomeEvent
    data class DeleteConversation(val conversation: Conversation) : HomeEvent
}