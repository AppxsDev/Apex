package com.appxs.apex.presentation.screen.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        modifier = Modifier.fillMaxSize().padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn (
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .weight(1F)
        ) {
            items(state.messages) { message ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = if (message.sender == Sender.User) 32.dp else 0.dp, end = if (message.sender == Sender.User) 0.dp else 32.dp),
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

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ChatScreenPreview() {
    ApexTheme(darkTheme = true) {
        ChatScreen(
            onEvent = {},
            state = ChatState(
                messages = listOf(
                    Message(id = 1, conversationId = 1, text = "Hello Apex AI. Can you help me? I have a lot of questions for you and I want you to solve them all please :)", sender = Sender.User, timestamp = 0),
                    Message(id = 2, conversationId = 1, text = "Hi there! Sure, just ask me everything you want and i'll answer your questions with the speed of the light because I am the best AI", sender = Sender.Ai, timestamp = 10),
                    Message(id = 3, conversationId = 1, text = "Yes? Then les see it. Who's Juice Wrld?", sender = Sender.User, timestamp = 20),
                    Message(id = 4, conversationId = 1, text = "Juice Wrld? The goat of rappers, but he died young but leyends never die. LLJW999", sender = Sender.Ai, timestamp = 30),
                    Message(id = 5, conversationId = 1, text = "Hello Apex AI. Can you help me? I have a lot of questions for you and I want you to solve them all please :)", sender = Sender.User, timestamp = 0),
                    Message(id = 6, conversationId = 1, text = "Hi there! Sure, just ask me everything you want and i'll answer your questions with the speed of the light because I am the best AI", sender = Sender.Ai, timestamp = 10),
                    Message(id = 7, conversationId = 1, text = "Yes? Then les see it. Who's Juice Wrld?", sender = Sender.User, timestamp = 20),
                    Message(id = 8, conversationId = 1, text = "Juice Wrld? The goat of rappers, but he died young but leyends never die. LLJW999", sender = Sender.Ai, timestamp = 30),
                    Message(id = 9, conversationId = 1, text = "Hello Apex AI. Can you help me? I have a lot of questions for you and I want you to solve them all please :)", sender = Sender.User, timestamp = 0),
                    Message(id = 10, conversationId = 1, text = "Hi there! Sure, just ask me everything you want and i'll answer your questions with the speed of the light because I am the best AI", sender = Sender.Ai, timestamp = 10),
                    Message(id = 11, conversationId = 1, text = "Yes? Then les see it. Who's Juice Wrld?", sender = Sender.User, timestamp = 20),
                    Message(id = 12, conversationId = 1, text = "Juice Wrld? The goat of rappers, but he died young but leyends never die. LLJW999", sender = Sender.Ai, timestamp = 30)
                ).reversed()
            )
        )
    }
}
