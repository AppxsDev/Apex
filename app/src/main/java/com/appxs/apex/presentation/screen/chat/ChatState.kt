package com.appxs.apex.presentation.screen.chat

import com.appxs.apex.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    var isLoading: Boolean = false
)