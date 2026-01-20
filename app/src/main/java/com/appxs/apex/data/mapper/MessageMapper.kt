package com.appxs.apex.data.mapper

import com.appxs.apex.data.datasource.local.entity.MessageEntity
import com.appxs.apex.data.datasource.remote.dto.AiResponseDto
import com.appxs.apex.domain.model.Feedback
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender

fun MessageEntity.toDomain(): Message =
    Message(
        id = id,
        conversationId = conversationId,
        text = text,
        sender = Sender.valueOf(sender),
        feedback = Feedback.valueOf(feedback),
        timestamp = timestamp,
        isRead = isRead
    )

fun Message.toEntity(): MessageEntity =
    MessageEntity(
        id = id,
        conversationId = conversationId,
        text = text,
        sender = sender.name,
        timestamp = timestamp,
        feedback = feedback.name,
        isRead = isRead
    )

fun AiResponseDto.toDomain(conversationId: Long): Message =
    Message(
        id = 0L,
        conversationId = conversationId,
        text = result.response,
        sender = Sender.Ai,
        feedback = Feedback.None,
        timestamp = System.currentTimeMillis(),
        isRead = false
    )
