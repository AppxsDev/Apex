package com.appxs.apex.domain.model

data class Conversation(
    val id: Long,
    val title: String?,
    val createdAt: Long,
    val lastMessageAt: Long
) {}