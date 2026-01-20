package com.appxs.apex.presentation.components

import android.content.Intent
import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appxs.apex.R
import com.appxs.apex.domain.model.Feedback
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender
import com.appxs.apex.presentation.util.parseMarkdown
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun AiMessageWidget(
    message: Message,
    shouldShowEffect: Boolean = false,
    onEffectFinished: () -> Unit = {},
    onGiveFeedback: (Feedback) -> Unit
) {
    // Split by whitespace but keep newlines
    val items = remember(message.text) { 
        message.text.split(Regex("(?<=\\s)|(?=\\s)")).filter { it.isNotEmpty() }
    }
    
    var count by remember(message.text, shouldShowEffect, message.isRead) { 
        mutableIntStateOf(if (shouldShowEffect && !message.isRead) 0 else items.size) 
    }
    
    var effectFinished by remember(message.text, shouldShowEffect, message.isRead) { 
        mutableStateOf(!(shouldShowEffect && !message.isRead)) 
    }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val textToSpeech = remember(context) {
        var tts: TextToSpeech? = null
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
        tts
    }

    DisposableEffect(key1 = textToSpeech) {
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    LaunchedEffect(message.text, shouldShowEffect, message.isRead) {
        if (shouldShowEffect && !message.isRead) {
            count = 0
            effectFinished = false
            for (i in items.indices) {
                count = i + 1
                delay(40) // Reduced delay since we split more granularly
            }
            effectFinished = true
            onEffectFinished()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val displayedText = items.take(count).joinToString("")
        Text(
            text = parseMarkdown(displayedText),
            fontSize = 16.sp
        )
        
        AnimatedVisibility(
            visible = effectFinished,
            enter = fadeIn()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AiMessageActionWidget(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(message.text))
                    },
                    icon = R.drawable.rounded_content_copy_24,
                    description = "Copy"
                )
                AiMessageActionWidget(
                    onClick = { 
                        textToSpeech.speak(message.text, TextToSpeech.QUEUE_FLUSH, null, null)
                    },
                    icon = R.drawable.rounded_volume_up_24,
                    description = "Speak"
                )
                AiMessageActionWidget(
                    onClick = {
                        onGiveFeedback(if (message.feedback == Feedback.Good) Feedback.None else Feedback.Good)
                    },
                    icon = if (message.feedback == Feedback.Good) R.drawable.round_thumb_up_24
                    else R.drawable.rounded_thumb_up_24,
                    description = "Like",
                    enabled = message.feedback != Feedback.Bad
                )
                AiMessageActionWidget(
                    onClick = {
                        onGiveFeedback(if (message.feedback == Feedback.Bad) Feedback.None else Feedback.Bad)
                    },
                    icon = if (message.feedback == Feedback.Bad) R.drawable.round_thumb_down_24
                    else R.drawable.rounded_thumb_down_24,
                    description = "Dislike",
                    enabled = message.feedback != Feedback.Good
                )
                AiMessageActionWidget(
                    onClick = { 
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, message.text)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                     },
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
        text = "Hello,\nthis is a **bold text** with a jump line.",
        sender = Sender.Ai,
        feedback = Feedback.None,
        timestamp = 0),
        onGiveFeedback = {}
    )
}
