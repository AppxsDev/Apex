package com.appxs.apex.domain.repository

import com.appxs.apex.data.datasource.local.entity.ConversationEntity
import com.appxs.apex.data.datasource.local.entity.MessageEntity
import com.appxs.apex.domain.model.Conversation
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun createConversation(title: String?) : Long
    fun getAllConversations() : Flow<List<ConversationEntity>>
    suspend fun updateConversation(conversation: Conversation) : ConversationEntity?
    suspend fun deleteConversation(conversation: Conversation)
    suspend fun insertMessage(text: String, conversationId: Long) : Long
    fun getAllMessagesFromConversation(conversationId: Long) : Flow<List<MessageEntity>>
}