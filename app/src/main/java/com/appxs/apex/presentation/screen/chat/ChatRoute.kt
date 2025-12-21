package com.appxs.apex.presentation.screen.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ChatRoute(
    conversationId: Long,
    viewModel: ChatViewModel = hiltViewModel()
) {
    viewModel.setConversationId(conversationId)
    val state by viewModel.state.collectAsStateWithLifecycle()
    ChatScreen(
        state = state,
    )
}