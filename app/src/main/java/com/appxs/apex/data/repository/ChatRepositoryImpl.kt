package com.appxs.apex.data.repository

import com.appxs.apex.core.time.SecureTimeDataSource
import com.appxs.apex.data.datasource.local.LocalChatDataSource
import com.appxs.apex.data.datasource.local.entity.ConversationEntity
import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.domain.repository.ChatRepository

class ChatRepositoryImpl(
    private val localChat: LocalChatDataSource,
    private val secureTime: SecureTimeDataSource
) : ChatRepository {
    override suspend fun createConversation(title: String?): Long {
        return localChat.createConversation(ConversationEntity(
            title = title,
            createdAt = secureTime.getCurrentTimeInMillis(),
            lastMessageAt = secureTime.getCurrentTimeInMillis()
        ))
    }

    override suspend fun getConversation(id: Int): Conversation? {
        return localChat.getConversation(id)
    }

    override suspend fun deleteConversation(id: Int) {
        TODO("Not yet implemented")
    }
}