package com.appxs.apex.data.repository

import com.appxs.apex.core.time.SecureTimeDataSource
import com.appxs.apex.data.datasource.local.LocalDataSource
import com.appxs.apex.data.datasource.local.entity.ConversationEntity
import com.appxs.apex.data.datasource.local.entity.MessageEntity
import com.appxs.apex.data.mapper.toDomain
import com.appxs.apex.data.mapper.toEntity
import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.domain.model.Feedback
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender
import com.appxs.apex.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform

class ChatRepositoryImpl(
    private val localChat: LocalDataSource,
    private val secureTime: SecureTimeDataSource
) : ChatRepository {
    override suspend fun createConversation(title: String?, firstMessage: String): Conversation {
        val conversation = ConversationEntity(
            title = title,
            createdAt = secureTime.getCurrentTimeInMillis(),
            lastMessageAt = secureTime.getCurrentTimeInMillis()
        )

        val newConversationId = localChat.createConversation(conversation)
        insertMessage(firstMessage, newConversationId)
        return conversation.copy(id = newConversationId).toDomain()
    }

    override fun getAllConversations(): Flow<List<Conversation>> {
        return localChat.getAllConversations()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override fun getConversation(id: Long): Flow<Conversation?> {
        return localChat.getAllConversations()
            .map { conversations ->
                conversations.find { it.id == id }?.toDomain()
            }
            .distinctUntilChanged()
    }

    override suspend fun updateConversation(conversation: Conversation) {
        return localChat.updateConversation(conversation.toEntity())
    }

    override suspend fun deleteConversation(conversation: Conversation) {
        return localChat.deleteConversation(conversation.toEntity())
    }

    override suspend fun insertMessage(text: String, conversationId: Long): Message {
        val message = MessageEntity(
            conversationId = conversationId,
            text = text,
            sender = Sender.User.toString(),
            timestamp = secureTime.getCurrentTimeInMillis(),
            feedback = Feedback.None.toString(),
            isRead = false
        )

        val newMessageId = localChat.createMessage(message)
        return message.copy(id = newMessageId).toDomain()
    }

    override fun getAllMessagesFromConversation(conversationId: Long): Flow<List<Message>> {
        return localChat.getAllMessagesOfConversation(conversationId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun updateMessage(message: Message) {
        return localChat.updateMessage(message.toEntity())
    }

    override suspend fun markMessageAsRead(messageId: Long) {
        localChat.markMessageAsRead(messageId)
    }
}
