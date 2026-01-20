package com.appxs.apex.presentation.screen.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appxs.apex.domain.model.Feedback
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender
import com.appxs.apex.presentation.components.AiMessageWidget
import com.appxs.apex.presentation.components.InputWidget
import com.appxs.apex.presentation.components.UserMessageWidget
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
            if (state.showThinking) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 32.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Thinking...",
                            modifier = Modifier.alpha(0.5f),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                        )
                    }
                }
            }

            itemsIndexed(state.messages) { index, message ->
                val isLastMessage = index == 0 // Since reverseLayout = true
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = if (message.sender == Sender.User) 32.dp else 0.dp,
                            end = if (message.sender == Sender.User) 0.dp else 32.dp),
                    horizontalArrangement = if (message.sender == Sender.User) Arrangement.End else Arrangement.Start
                ) {
                    if (message.sender == Sender.User) {
                        UserMessageWidget(message)
                    } else {
                        AiMessageWidget(
                            message = message,
                            shouldShowEffect = isLastMessage && state.isEffectRunning,
                            onEffectFinished = {
                                onEvent(ChatEvent.MessageEffectFinished(message.id))
                            },
                            onGiveFeedback = { feedback ->
                                onEvent(ChatEvent.GiveFeedback(message, feedback))
                            }
                        )
                    }
                }
            }
        }
        InputWidget(
            enabled = !state.isInputBlocked,
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
                showThinking = true,
                messages = listOf(
                    Message(id = 1, conversationId = 1, text = "Hello Apex AI. Can you help me? I have a lot of questions for you and I want you to solve them all please :)", sender = Sender.User, timestamp = 0, feedback = Feedback.None),
                    Message(id = 2, conversationId = 1, text = "Hi there! Sure, just ask me everything you want and i'll answer your questions with the speed of the light because I am the best AI", sender = Sender.Ai, timestamp = 10, feedback = Feedback.None),
                ).reversed()
            )
        )
    }
}
