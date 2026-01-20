package com.appxs.apex.presentation.screen.chat

import com.appxs.apex.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val isEffectRunning: Boolean = false,
    val showThinking: Boolean = false
) {
    val isInputBlocked: Boolean get() = isLoading || isEffectRunning
}
