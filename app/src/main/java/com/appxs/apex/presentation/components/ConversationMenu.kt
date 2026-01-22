package com.appxs.apex.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appxs.apex.domain.model.Conversation
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()
    
    // Animate color from black to white
    val scrimColor by animateColorAsState(
        targetValue = if (drawerState.targetValue != DrawerValue.Closed) Color.White else Color.Black,
        label = "drawer_scrim_color"
    )
    
    // Animate opacity up to 10%
    val scrimAlpha by animateFloatAsState(
        targetValue = if (drawerState.targetValue != DrawerValue.Closed) 0.1f else 0f,
        label = "drawer_scrim_alpha"
    )

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet(
                drawerTonalElevation = 8.dp,
                modifier = Modifier
                    .shadow(8.dp, shape = DrawerDefaults.shape)
            ) {
                Row(
                    modifier = Modifier
                        .padding(all = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = "",
                        onValueChange = { },
                        placeholder = { 
                            Text(
                                "Search...",
                                modifier = Modifier.alpha(0.75f),
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
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
                            cursorColor = MaterialTheme.colorScheme.onPrimary,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
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
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                content()
                if (scrimAlpha > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(scrimColor.copy(alpha = scrimAlpha))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                scope.launch { drawerState.close() }
                            }
                    )
                }
            }
        }
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
    ) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
    }
}
