package com.appxs.apex.presentation.screen.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender
import com.appxs.apex.presentation.components.InputWidget
import com.appxs.apex.presentation.components.MessageBubble
import com.appxs.apex.presentation.screen.home.HomeEvent
import com.appxs.apex.presentation.ui.theme.ApexTheme

@Composable
fun ChatScreen(
    state: ChatState,
    onEvent: (ChatEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn (
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1F)
        ) {
            items(state.messages) { message ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (message.sender == Sender.User) Arrangement.End else Arrangement.Start
                ) {
                    MessageBubble(message)
                }
            }
        }
        InputWidget(
            onSend = { message -> onEvent(ChatEvent.MessageSent(message))}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenPreview() {
    ApexTheme(darkTheme = true) {
        ChatScreen(
            onEvent = {},
            state = ChatState(
                messages = listOf(
                    Message(id = 1, conversationId = 1, text = "Hello", sender = Sender.User, timestamp = 0),
                    Message(id = 2, conversationId = 1, text = "Hi there!", sender = Sender.Ai, timestamp = 10),
                    Message(id = 3, conversationId = 1, text = "How are you?", sender = Sender.User, timestamp = 10),
                    Message(id = 4, conversationId = 1, text = "Fine and you?", sender = Sender.Ai, timestamp = 10)
                )
            )
        )
    }
}
