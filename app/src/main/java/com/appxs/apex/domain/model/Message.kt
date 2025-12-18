package com.appxs.apex.domain.model

data class Message(
    val id: Long,
    val conversationId: Long,
    val text: String,
    val sender: Sender,
    val timestamp: Long
) {

}