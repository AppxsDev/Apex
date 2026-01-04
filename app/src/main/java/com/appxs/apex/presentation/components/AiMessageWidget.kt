package com.appxs.apex.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appxs.apex.R
import com.appxs.apex.domain.model.Feedback
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender
import kotlinx.coroutines.delay

@Composable
fun AiMessageWidget(message: Message) {
    val words = remember(message.text) { message.text.split(Regex("\\s+")) }
    var count by remember(message.text) { mutableIntStateOf(0) }

    LaunchedEffect(message.text) {
        count = 0
        for (i in words.indices) {
            count = i + 1
            delay(80)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = words.take(count).joinToString(" "),
            fontSize = 16.sp,
            color = Color.hsl(0F, 0F, .9F))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AiMessageActionWidget(
                onClick = { },
                icon = R.drawable.rounded_content_copy_24,
                description = "Copy"
            )
            AiMessageActionWidget(
                onClick = { },
                icon = R.drawable.rounded_volume_up_24,
                description = "Speak"
            )
            AiMessageActionWidget(
                onClick = { },
                icon = if (message.feedback == Feedback.Good) R.drawable.round_thumb_up_24
                else R.drawable.rounded_thumb_up_24,
                description = "Like",
                enabled = message.feedback != Feedback.Bad
            )
            AiMessageActionWidget(
                onClick = { },
                icon = if (message.feedback == Feedback.Bad) R.drawable.round_thumb_down_24
                else R.drawable.rounded_thumb_down_24,
                description = "Dislike",
                enabled = message.feedback != Feedback.Good
            )
            AiMessageActionWidget(
                onClick = { },
                icon = R.drawable.rounded_share_24,
                description = "Share"
            )
            AiMessageActionWidget(
                onClick = { },
                icon = R.drawable.rounded_more_horiz_24,
                description = "More"
            )
        }
    }
}

@Composable
fun AiMessageActionWidget(onClick: () -> Unit, icon: Int, description: String, enabled: Boolean = true) {
    IconButton(
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier.size(24.dp)) {
        Icon(
            painter = painterResource(icon),
            contentDescription = description,
            tint = Color.hsl(0F, 0F, .85F),
            modifier = Modifier.size(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun UserMessageWidgetPreview() {
    AiMessageWidget(message = Message(
        id = 1,
        conversationId = 1,
        text = "Hello, this is an long text used for example preview of Apex, the best AI of the world.",
        sender = Sender.Ai,
        feedback = Feedback.None,
        timestamp = 0)
    )
}