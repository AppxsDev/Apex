package com.appxs.apex.domain.repository

import com.appxs.apex.domain.model.Message

interface AiRepository {
    suspend fun sendMessage(text: String, conversationId: Long, history: List<Message> = emptyList()): Result<Message>
}
