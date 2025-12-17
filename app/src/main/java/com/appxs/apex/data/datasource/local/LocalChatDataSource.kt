package com.appxs.apex.data.datasource.local

import com.appxs.apex.data.datasource.local.dao.ConversationDao
import com.appxs.apex.data.datasource.local.entity.ConversationEntity

class LocalChatDataSource(
    private val conversationDao: ConversationDao
) {
    suspend fun createConversation(conversation: ConversationEntity) : Long {
        return conversationDao.insert(conversation)
    }

    suspend fun getConversation(id: Long) : ConversationEntity? {
        return conversationDao.getById(id)
    }

    suspend fun deleteConversation(conversation: ConversationEntity) {
        return conversationDao.delete(conversation)
    }
}