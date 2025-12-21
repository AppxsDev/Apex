package com.appxs.apex.domain.usecase.chat

import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.domain.repository.ChatRepository

class CreateConversationUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(title: String?, firstMessage: String): Conversation {
        return chatRepository.createConversation(title, firstMessage)
    }
}