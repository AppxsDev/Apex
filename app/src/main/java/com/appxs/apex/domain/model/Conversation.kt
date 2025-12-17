package com.appxs.apex.domain.model

data class Conversation(
    val id: Int,
    val title: String?,
    val createdAt: Long,
    val lastMessageAt: Long
) {}