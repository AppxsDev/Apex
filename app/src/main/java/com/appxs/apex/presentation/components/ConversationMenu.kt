package com.appxs.apex.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appxs.apex.domain.model.Conversation

@Composable
fun ConversationMenu(
    selectedConversationId: Long?,
    conversations: List<Conversation>,
    drawerState: DrawerState,
    onConversationClick: (Conversation) -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Apex", modifier = Modifier.padding(16.dp))
                LazyColumn {
                    items(conversations) { conversation ->
                        ConversationRow(
                            conversation = conversation,
                            onClick = { onConversationClick(conversation) },
                            selectedConversationId = selectedConversationId
                        )
                    }
                }
            }
        },
        content = content
    )
}

@Preview
@Composable
private fun ConversationMenuPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val sampleConversations = listOf(
        Conversation(title = "Welcome to Apex", id = 0L, createdAt = 0L, lastMessageAt = 0L),
        Conversation(title = "Getting Started", id = 1L, createdAt = 10L, lastMessageAt = 10L)
    )

    ConversationMenu(
        selectedConversationId = null,
        conversations = sampleConversations,
        drawerState = drawerState,
        onConversationClick = {}
    ) {}
}
