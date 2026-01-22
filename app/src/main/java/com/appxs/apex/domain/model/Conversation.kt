package com.appxs.apex.domain.model

enum class ConversationType {
    Saved,
    Temp
}

data class Conversation(
    val id: Long,
    val title: String?,
    val createdAt: Long,
    val lastMessageAt: Long,
    val type: ConversationType = ConversationType.Saved
) {}