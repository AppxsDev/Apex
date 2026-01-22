package com.appxs.apex.data.mapper

import com.appxs.apex.data.datasource.local.entity.ConversationEntity
import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.domain.model.ConversationType

fun ConversationEntity.toDomain(): Conversation =
    Conversation(
        id = id,
        title = title,
        createdAt = createdAt,
        lastMessageAt = lastMessageAt,
        type = ConversationType.valueOf(type)
    )

fun Conversation.toEntity(): ConversationEntity =
    ConversationEntity(
        id = id,
        title = title,
        createdAt = createdAt,
        lastMessageAt = lastMessageAt,
        type = type.name
    )