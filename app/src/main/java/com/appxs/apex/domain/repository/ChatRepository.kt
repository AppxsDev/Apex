package com.appxs.apex.domain.repository

import com.appxs.apex.domain.model.Conversation

interface ChatRepository {
    suspend fun createConversation(title: String?) : Long
    suspend fun getConversation(id: Int) : Conversation?
    suspend fun deleteConversation(id: Int)
}