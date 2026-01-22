package com.appxs.apex.data.datasource.local

import com.appxs.apex.data.datasource.local.dao.ConversationDao
import com.appxs.apex.data.datasource.local.dao.MessageDao
import com.appxs.apex.data.datasource.local.entity.ConversationEntity
import com.appxs.apex.data.datasource.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao
) {
    suspend fun createConversation(conversation: ConversationEntity): Long {
        return conversationDao.insert(conversation)
    }

    fun getAllConversations() : Flow<List<ConversationEntity>> {
        return conversationDao.getAll()
    }

    suspend fun getConversation(id: Long): ConversationEntity? {
        return conversationDao.getById(id)
    }

    suspend fun updateConversation(conversation: ConversationEntity) {
        return conversationDao.update(conversation)
    }

    suspend fun deleteConversation(conversation: ConversationEntity) {
        return conversationDao.delete(conversation)
    }

    fun getAllMessagesOfConversation(conversationId: Long) : Flow<List<MessageEntity>> {
        return messageDao.getAllMessagesOfConversation(conversationId)
    }

    suspend fun createMessage(message: MessageEntity): Long {
        return messageDao.insert(message)
    }

    suspend fun updateMessage(message: MessageEntity) {
        return messageDao.update(message)
    }

    suspend fun markMessageAsRead(messageId: Long) {
        messageDao.markAsRead(messageId)
    }

    suspend fun deleteTemporalConversations() {
        conversationDao.deleteTemporal()
    }
}