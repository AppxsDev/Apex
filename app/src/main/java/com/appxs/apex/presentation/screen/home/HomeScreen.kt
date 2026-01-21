package com.appxs.apex.presentation.screen.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appxs.apex.R
import com.appxs.apex.domain.model.Conversation
import com.appxs.apex.presentation.components.ConversationMenu
import com.appxs.apex.presentation.screen.chat.NewChatScreen
import com.appxs.apex.presentation.ui.theme.ApexTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    chatContent: @Composable (Long) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val haptic = LocalHapticFeedback.current

    ConversationMenu(
        selectedConversationId = state.selectedConversationId,
        conversations = state.conversations,
        drawerState = drawerState,
        onNewConversation = {
            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
            onNewConversation(onEvent)
            scope.launch { drawerState.close() }
        },
        onConversationClick = { conversation ->
            onEvent(HomeEvent.ConversationSelected(conversation.id))
            scope.launch { drawerState.close() }
        }
    ) {
        Scaffold(
            modifier = Modifier.imePadding(),
            contentWindowInsets = WindowInsets(0),
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Apex AI", textAlign = TextAlign.Center)
                                Text(
                                    "1.0",
                                    style = TextStyle(fontSize = 14.sp),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.alpha(0.5f)
                                )
                            }
                            if (state.temporal)
                                Text("Temporal chat",
                                    style = TextStyle(fontSize = 10.sp),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.alpha(0.5f))
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { 
                            keyboardController?.hide()
                            scope.launch { drawerState.open() } 
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    },
                    actions = {
                        if (state.selectedConversationId != null) {
                            IconButton(onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                onNewConversation(onEvent) })
                            {
                                Icon(Icons.Default.Add, contentDescription = null)
                            }
                            IconButton(onClick = { }) {
                                Icon(
                                    painter = painterResource(R.drawable.rounded_more_horiz_24),
                                    contentDescription = null
                                )
                            }
                        } else {
                            IconButton(onClick = { onEvent(HomeEvent.ChangedTemporalConversation) }) {
                                Icon(painterResource(if (state.temporal) R.drawable.round_blur_off_24 else R.drawable.round_blur_on_24), contentDescription = null)
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .imePadding()
                            .fillMaxWidth()
                    ) {
                        val selectedId = state.selectedConversationId
                        if (selectedId != null) {
                            chatContent(selectedId)
                        } else {
                            NewChatScreen(
                                temporal = state.temporal,
                                onSend = { message ->
                                    onEvent(HomeEvent.ConversationCreated(message))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

fun onNewConversation(onEvent: (HomeEvent) -> Unit) {
    onEvent(HomeEvent.NewChatClicked)
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ApexTheme {
        HomeScreen(
            onEvent = {},
            chatContent = { },
            state = HomeState(
                temporal = true,
                conversations = listOf(
                    Conversation(id = 1, title = "Sample Conversation 1", createdAt = 0L, lastMessageAt = 0L),
                    Conversation(id = 2, title = "Sample Conversation 2", createdAt = 1L, lastMessageAt = 1L)
                )
            ),
        )
    }
}
