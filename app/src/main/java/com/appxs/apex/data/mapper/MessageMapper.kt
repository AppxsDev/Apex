package com.appxs.apex.data.mapper

import com.appxs.apex.data.datasource.local.entity.MessageEntity
import com.appxs.apex.data.datasource.remote.dto.MessageDto
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender

fun MessageEntity.toDomain(): Message =
    Message(
        id = id,
        conversationId = conversationId,
        text = text,
        sender = Sender.valueOf(sender),
        timestamp = timestamp)

fun Message.toEntity(): MessageEntity =
    MessageEntity(
        id = id,
        conversationId = conversationId,
        text = text,
        sender = sender.name,
        timestamp = timestamp
    )

fun MessageDto.toDomain(conversationId: Long): Message =
    Message(
        id = 0L,
        conversationId = conversationId,
        text = content,
        sender = role.toSender(),
        timestamp = System.currentTimeMillis()
    )