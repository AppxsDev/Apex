package com.appxs.apex.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appxs.apex.domain.model.Conversation

@Composable
fun ConversationRow(
    selectedConversationId: Long?,
    conversation: Conversation,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(text = conversation.title ?: ("Conversation " + conversation.id)) },
        selected = selectedConversationId == conversation.id,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp)
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
