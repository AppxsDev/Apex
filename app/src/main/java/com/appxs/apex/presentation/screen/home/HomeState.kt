package com.appxs.apex.presentation.screen.home

import com.appxs.apex.domain.model.Conversation

data class HomeState(
    val conversations: List<Conversation> = emptyList(),
    val selectedConversationId: Long? = null,
    val temporal: Boolean = false
)
