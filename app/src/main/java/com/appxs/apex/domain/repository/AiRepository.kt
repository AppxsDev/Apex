package com.appxs.apex.domain.repository

import com.appxs.apex.domain.model.Message

interface AiRepository {
    suspend fun sendMessage(text: String, conversationId: Long): Result<Message>
}