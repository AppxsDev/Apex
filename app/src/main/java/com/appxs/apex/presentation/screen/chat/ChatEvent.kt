package com.appxs.apex.presentation.screen.chat

sealed interface ChatEvent {
    data class MessageSent(val message: String) : ChatEvent
}