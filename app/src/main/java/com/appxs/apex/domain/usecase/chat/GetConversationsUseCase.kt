package com.appxs.apex.domain.usecase.chat

import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetConversationsUseCase(private val chatRepository: ChatRepository) {
    operator fun invoke(): Flow<List<Conversation>> {
        return chatRepository.getAllConversations()
    }
}