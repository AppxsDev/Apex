package com.appxs.apex.domain.repository

import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun createConversation(title: String?) : Long
    fun getAllConversations() : Flow<List<Conversation>>
    suspend fun updateConversation(conversation: Conversation) : Conversation?
    suspend fun deleteConversation(conversation: Conversation)
    suspend fun insertMessage(text: String, conversationId: Long) : Long
    fun getAllMessagesFromConversation(conversationId: Long) : Flow<List<Message>>
}