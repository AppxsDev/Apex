package com.appxs.apex.domain.usecase.chat

import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.repository.ChatRepository

class GiveFeedbackUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(message: Message) {
        return chatRepository.updateMessage(message)
    }
}