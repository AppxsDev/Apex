package com.appxs.apex.data.datasource.local

import com.appxs.apex.data.datasource.local.dao.ConversationDao
import com.appxs.apex.data.datasource.local.dao.MessageDao
import com.appxs.apex.data.datasource.local.entity.ConversationEntity
import com.appxs.apex.data.datasource.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

class LocalChatDataSource(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao
) {
    suspend fun createConversation(conversation: ConversationEntity) : Long {
        return conversationDao.insert(conversation)
    }

    fun getAllConversations() : Flow<List<ConversationEntity>> {
        return conversationDao.getAll()
    }

    suspend fun updateConversation(conversation: ConversationEntity) : ConversationEntity? {
        return conversationDao.update(conversation)
    }

    suspend fun deleteConversation(conversation: ConversationEntity) {
        return conversationDao.delete(conversation)
    }

    fun getAllMessagesOfConversation(conversationId: Long) : Flow<List<MessageEntity>> {
        return messageDao.getAllOfConversation(conversationId)
    }

    suspend fun insertMessage(message: MessageEntity) : Long {
        return messageDao.insert(message)
    }
}