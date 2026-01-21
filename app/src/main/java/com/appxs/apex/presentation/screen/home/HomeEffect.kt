package com.appxs.apex.presentation.screen.home

sealed interface HomeEffect {
    data class SendAiForFirstMessage(
        val conversationId: Long,
        val firstMessage: String
    ) : HomeEffect
}