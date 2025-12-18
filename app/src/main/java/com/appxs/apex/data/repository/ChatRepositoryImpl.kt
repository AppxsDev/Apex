package com.appxs.apex.data.repository

import com.appxs.apex.core.time.SecureTimeDataSource
import com.appxs.apex.data.datasource.local.LocalChatDataSource
import com.appxs.apex.data.datasource.local.entity.ConversationEntity
import com.appxs.apex.data.datasource.local.entity.MessageEntity
import com.appxs.apex.data.mapper.toEntity
import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.domain.model.Sender
import com.appxs.apex.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

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

    override fun getAllConversations(): Flow<List<ConversationEntity>> {
        return localChat.getAllConversations()
    }

    override suspend fun updateConversation(conversation: Conversation): ConversationEntity? {
        return localChat.updateConversation(conversation.toEntity())
    }

    override suspend fun deleteConversation(conversation: Conversation) {
        return localChat.deleteConversation(conversation.toEntity())
    }

    override suspend fun insertMessage(text: String, conversationId: Long): Long {
        return localChat.insertMessage(MessageEntity(
            conversationId = conversationId,
            text = text,
            sender = Sender.User.toString(),
            timestamp = secureTime.getCurrentTimeInMillis(),
        ))
    }

    override fun getAllMessagesFromConversation(conversationId: Long): Flow<List<MessageEntity>> {
        return localChat.getAllMessagesOfConversation(conversationId)
    }
}