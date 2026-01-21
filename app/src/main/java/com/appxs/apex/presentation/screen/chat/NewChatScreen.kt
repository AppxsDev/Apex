package com.appxs.apex.presentation.screen.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.appxs.apex.R
import com.appxs.apex.presentation.components.InputWidget

@Composable
fun NewChatScreen(
    onSend: (message: String) -> Unit,
    temporal: Boolean
) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.apex_logo))

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1F)
                .offset(y = (-50).dp)
                .padding(horizontal = 32.dp),
        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(150.dp)
            )
            Text(
                text = "Apex AI. The Future of AI is here",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "I am a powerful AI assistant that can help you with a variety of tasks. Any question? Just write it and I'll help you.",
                fontSize = 14.sp,
                color = Color.hsl(0F, 0F, .65F),
                textAlign = TextAlign.Center
            )
            if (temporal) {
                Spacer(modifier = Modifier.size(16.dp))
                Card(
                    modifier = Modifier
                        .alpha(0.5f),
                    shape = RoundedCornerShape(size = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(all = 8.dp),
                        ) {
                        Icon(Icons.Rounded.Info, contentDescription = "", modifier = Modifier.size(16.dp))
                        Text("The conversation won't be saved with temporal mode.",
                            style = TextStyle(
                                fontSize = 11.sp
                            ))
                    }
                }
            }
        }
        InputWidget(
            onSend = onSend
        )
    }
}

@Preview
@Composable
fun NewChatScreenPreview() {
    return NewChatScreen(
        onSend = { message -> },
        temporal = true
    )
}
