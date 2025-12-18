package com.appxs.apex.domain.usecase.chat

import com.appxs.apex.domain.repository.ChatRepository

class SendMessageUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(message: String, conversationId: Long): Long{
        return chatRepository.insertMessage(message, conversationId)
    }
}