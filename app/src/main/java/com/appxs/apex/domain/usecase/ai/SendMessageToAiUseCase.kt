package com.appxs.apex.domain.usecase.ai

import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.repository.AiRepository

class SendMessageToAiUseCase(private val aiRepository: AiRepository) {
    suspend operator fun invoke(message: String, conversationId: Long) : Result<Message> {
        return aiRepository.sendMessage(message, conversationId)
    }
}