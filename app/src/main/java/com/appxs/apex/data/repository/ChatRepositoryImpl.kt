package com.appxs.apex.data.repository

import com.appxs.apex.core.time.SecureTimeDataSource
import com.appxs.apex.data.datasource.local.LocalChatDataSource
import com.appxs.apex.data.datasource.local.entity.ConversationEntity
import com.appxs.apex.data.datasource.local.entity.MessageEntity
import com.appxs.apex.data.mapper.toDomain
import com.appxs.apex.data.mapper.toEntity
import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender
import com.appxs.apex.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(
    private val localChat: LocalChatDataSource,
    private val secureTime: SecureTimeDataSource
) : ChatRepository {
    override suspend fun createConversation(title: String?) {
        return localChat.createConversation(ConversationEntity(
            title = title,
            createdAt = secureTime.getCurrentTimeInMillis(),
            lastMessageAt = secureTime.getCurrentTimeInMillis()
        ))
    }

    override fun getAllConversations(): Flow<List<Conversation>> {
        return localChat.getAllConversations()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun updateConversation(conversation: Conversation) {
        return localChat.updateConversation(conversation.toEntity())
    }

    override suspend fun deleteConversation(conversation: Conversation) {
        return localChat.deleteConversation(conversation.toEntity())
    }

    override suspend fun insertMessage(text: String, conversationId: Long) {
        return localChat.insertMessage(MessageEntity(
            conversationId = conversationId,
            text = text,
            sender = Sender.User.toString(),
            timestamp = secureTime.getCurrentTimeInMillis(),
        ))
    }

    override fun getAllMessagesFromConversation(conversationId: Long): Flow<List<Message>> {
        return localChat.getAllMessagesOfConversation(conversationId)
            .map { entities -> entities.map { it.toDomain() } }
    }
}