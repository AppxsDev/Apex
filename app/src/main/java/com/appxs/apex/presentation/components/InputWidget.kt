package com.appxs.apex.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputWidget() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            border = BorderStroke(width = 1.dp, color = Color.hsl(0F, 0F, .15F)),
            colors = CardDefaults.cardColors(
                containerColor = Color.hsl(0F, 0F, .1F)
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
        ) {
            IconButton(onClick = {}) { Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Record",
                tint = Color.hsl(0F, 0F, .9F),
                modifier = Modifier.size(24.dp)
            )}

        }
        InputTextWidget()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextWidget() {
    var input by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        border = BorderStroke(width = 1.dp, color = Color.hsl(0F, 0F, .15F)),
        colors = CardDefaults.cardColors(
            containerColor = Color.hsl(0F, 0F, .1F)
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            Modifier.padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .weight(1f)
                    .height(48.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle.Default.copy(fontSize = 14.sp, color = Color.hsl(0F, 0F, .95F)),
                value = input,
                singleLine = true,
                placeholder = { Text("Ask to Apex...",
                    style = TextStyle.Default.copy(fontSize = 14.sp, color = Color.hsl(0F, 0F, .65F))) },
                onValueChange = {
                    input = it
                }
            )
            Icon(imageVector = Icons.AutoMirrored.Rounded.Send, contentDescription = "Send", tint = Color.hsl(0F, 0F, .9F))
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
private fun InputWidgetPreview() {
    InputWidget()
}
