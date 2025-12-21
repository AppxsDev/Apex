package com.appxs.apex.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.presentation.components.ConversationMenu
import com.appxs.apex.presentation.components.InputWidget
import com.appxs.apex.presentation.screen.chat.ChatRoute
import com.appxs.apex.presentation.screen.chat.NewChatScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ConversationMenu(
        selectedConversationId = state.selectedConversationId,
        conversations = state.conversations,
        drawerState = drawerState,
        onConversationClick = { onEvent(HomeEvent.ConversationSelected(it.id)) }
    ) {
        Scaffold(
            modifier = Modifier.imePadding(),
            contentWindowInsets = WindowInsets(0), // <-- important: keep topBar pinned
            topBar = {
                TopAppBar(
                    title = { Text("Chat") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // includes topBar height
                    .windowInsetsPadding(WindowInsets.statusBars) // optional if you draw behind status bar
                    .padding(16.dp)
            ) {
                // Message/content area shrinks when IME opens
                Box(
                    modifier = Modifier
                        .imePadding()
                        .fillMaxWidth()
                ) {
                    if (state.selectedConversationId != null)
                        ChatRoute(
                            conversationId = state.selectedConversationId)
                    else
                        NewChatScreen()
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        onEvent = {},
        state = HomeState(
            conversations = listOf(
                Conversation(id = 1, title = "Sample Conversation 1", createdAt = 0L, lastMessageAt = 0L),
                Conversation(id = 2, title = "Sample Conversation 2", createdAt = 1L, lastMessageAt = 1L)
            )
        ),
    )
}
