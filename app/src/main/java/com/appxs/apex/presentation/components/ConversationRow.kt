package com.appxs.apex.presentation.components

import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.appxs.apex.domain.model.Conversation

@Composable
fun ConversationRow(
    selectedConversationId: Long?,
    conversation: Conversation,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { conversation.title?.let { Text(text = it) } },
        selected = selectedConversationId == conversation.id,
        onClick = onClick
    )
}

@Preview
@Composable
private fun ConversationRowPreview() {
    ConversationRow(
        selectedConversationId = null,
        conversation = Conversation(title = "Sample Conversation", id = 1L, createdAt = 0L, lastMessageAt = 0L),
        onClick = {}
    )
}
