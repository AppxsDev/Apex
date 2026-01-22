package com.appxs.apex.domain.usecase.chat

import com.appxs.apex.domain.repository.ChatRepository

class DeleteTemporalConversationsUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke() {
        return chatRepository.deleteTemporalConversations()
    }
}