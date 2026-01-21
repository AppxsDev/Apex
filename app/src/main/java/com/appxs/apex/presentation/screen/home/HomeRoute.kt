package com.appxs.apex.presentation.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appxs.apex.presentation.screen.chat.ChatRoute
import com.appxs.apex.presentation.screen.chat.ChatViewModel

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel(),
    ) {
    val state by homeViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(homeViewModel) {
        homeViewModel.effects.collect { effect ->
            when (effect) {
                is HomeEffect.SendAiForFirstMessage -> {
                    chatViewModel.sendFirstMessageToAi(
                        conversationId = effect.conversationId,
                        firstMessage = effect.firstMessage
                    )
                }
            }
        }
    }

    HomeScreen(
        state = state,
        onEvent = homeViewModel::onEvent,
        chatContent = { conversationId ->
            ChatRoute(
                conversationId = conversationId,
                viewModel = chatViewModel
            )
        }
    )
}