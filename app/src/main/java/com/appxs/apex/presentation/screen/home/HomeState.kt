package com.appxs.apex.presentation.screen.home

import com.appxs.apex.domain.model.Conversation

data class HomeState(
    val conversations: List<Conversation> = emptyList(),
    val selectedConversationId: Long? = null,

    // val messages: List<Message> = emptyList(),
    // val isLoading: Boolean = false,
    // val error: String? = null,

    // val inputText: String = ""
)
