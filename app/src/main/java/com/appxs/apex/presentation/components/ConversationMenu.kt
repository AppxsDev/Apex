package com.appxs.apex.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appxs.apex.domain.model.Conversation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationMenu(
    selectedConversationId: Long?,
    conversations: List<Conversation>,
    drawerState: DrawerState,
    onNewConversation: () -> Unit,
    onConversationClick: (Conversation) -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Using TextField instead of SearchBar to have full control over the 48dp height
                    // as SearchBar has fixed internal padding that cuts text at smaller heights.
                    TextField(
                        value = "",
                        onValueChange = { },
                        placeholder = { 
                            Text(
                                "Search...", 
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
                            ) 
                        },
                        leadingIcon = { 
                            Icon(
                                Icons.Default.Search, 
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            ) 
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
                    )

                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.size(48.dp)
                    ) {
                        IconButton(
                            onClick = onNewConversation,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "New Chat",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(conversations) { conversation ->
                        ConversationRow(
                            conversation = conversation,
                            onClick = { onConversationClick(conversation) },
                            selectedConversationId = selectedConversationId
                        )
                        Spacer(modifier = Modifier.height(8.dp))
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
        onConversationClick = {},
        onNewConversation = {}
    ) {}
}
