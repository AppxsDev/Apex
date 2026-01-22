package com.appxs.apex.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appxs.apex.domain.model.Conversation

@Composable
fun ConversationRow(
    selectedConversationId: Long?,
    conversation: Conversation,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        shape = RoundedCornerShape(16.dp),
        label = { Text(text = conversation.title ?: ("Conversation " + conversation.id), style = TextStyle(fontSize = 14.sp)) },
        selected = selectedConversationId == conversation.id,
        onClick = onClick,
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
