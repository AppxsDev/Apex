package com.appxs.apex.presentation.screen.chat

import com.appxs.apex.domain.model.Feedback
import com.appxs.apex.domain.model.Message

sealed interface ChatEvent {
    data class MessageSent(val message: String) : ChatEvent
    data class GiveFeedback(val message: Message, val feedback: Feedback) : ChatEvent
    data object AiResponseReceived : ChatEvent
    data object AiResponseShowed : ChatEvent
}