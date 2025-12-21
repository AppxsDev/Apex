package com.appxs.apex.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender

@Composable
fun MessageBubble(message: Message) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = if (message.sender == Sender.User) {
                Color.hsl(0F, 0F, .1F)
            } else {
                Color.Transparent
            }
        )
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(message.text,
                fontSize = 14.sp,
                color = Color.hsl(0F, 0F, .95F))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageBubblePreview() {
    MessageBubble(message = Message(
        id = 1,
        conversationId = 1,
        text = "Hello, this is an long text used for example preview of Apex, the best AI of the world.",
        sender = Sender.User,
        timestamp = 0)
    )
}