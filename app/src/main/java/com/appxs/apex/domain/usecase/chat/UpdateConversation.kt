package com.appxs.apex.domain.usecase.chat

import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.domain.repository.ChatRepository

class UpdateConversation(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(conversation: Conversation): Conversation? {
        return chatRepository.updateConversation(conversation)
    }
}