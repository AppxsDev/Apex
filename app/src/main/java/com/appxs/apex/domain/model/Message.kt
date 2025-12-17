package com.appxs.apex.domain.model

data class Message(
    val conversationId: Int,
    val text: String,
    val sender: Sender,
    val timestamp: Long
) {

}