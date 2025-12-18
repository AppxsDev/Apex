package com.appxs.apex.domain.usecase.chat

import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetMessagesUseCase(private val chatRepository: ChatRepository) {
    operator fun invoke(conversationId: Long): Flow<List<Message>> {
        return chatRepository.getAllMessagesFromConversation(conversationId)
    }
}