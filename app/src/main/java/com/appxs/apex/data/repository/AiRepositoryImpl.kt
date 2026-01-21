package com.appxs.apex.data.repository

import com.appxs.apex.core.rest.RestResult
import com.appxs.apex.core.time.SecureTimeDataSource
import com.appxs.apex.data.datasource.local.LocalDataSource
import com.appxs.apex.data.datasource.remote.RemoteDataSource
import com.appxs.apex.data.datasource.remote.dto.MessageDto
import com.appxs.apex.data.datasource.remote.dto.StructuredAiResponseDto
import com.appxs.apex.data.mapper.toDomain
import com.appxs.apex.data.mapper.toEntity
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender
import com.appxs.apex.domain.repository.AiRepository
import kotlinx.serialization.json.Json

class AiRepositoryImpl(
    private val remoteChat: RemoteDataSource,
    private val localChat: LocalDataSource,
    private val secureTime: SecureTimeDataSource): AiRepository {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    override suspend fun sendMessage(text: String, conversationId: Long, history: List<Message>): Result<Message> {
        val currentTime = secureTime.getCurrentTimeInMillis()
        val isFirstMessage = history.isEmpty()

        val systemContent = if (isFirstMessage) {
            "Your name is Apex. You are a helpful assistant. You must answer clearly as much as possible. You must answer in the same language of the user. Since this is the start of the conversation, you MUST provide your response strictly in JSON format with two keys: \"title\" (a short title for this conversation based on the user's message and also in the same language of the user's message. Do it in 5 words) and \"message\" (your response). Example: {\"title\": \"Greetings\", \"message\": \"Hello! how can I help you today?\"}. Do not include any other text or explanation outside the JSON."
        } else {
            "Your name is Apex. You must answer clearly as much as possible. You must answer in the same language of the user. You are a helpful assistant."
        }
        
        val systemMessage = MessageDto(
            role = "system",
            content = systemContent,
            timestamp = currentTime
        )

        val historyDtos = history.map { 
            MessageDto(
                role = if (it.sender == Sender.User) "user" else "assistant",
                content = it.text,
                timestamp = it.timestamp
            )
        }

        val userMessage = MessageDto(
            role = "user",
            content = text,
            timestamp = currentTime
        )

        val allMessages = listOf(systemMessage) + historyDtos + userMessage

        return when (val res = remoteChat.sendMessage(allMessages)) {
            is RestResult.Success -> {
                var responseText = res.value.result.response
                
                if (isFirstMessage) {
                    try {
                        val jsonMatch = Regex("""\{.*\}""", RegexOption.DOT_MATCHES_ALL).find(responseText)
                        val jsonString = jsonMatch?.value ?: responseText
                        
                        val structured = json.decodeFromString<StructuredAiResponseDto>(jsonString)
                        responseText = structured.message
                        
                        localChat.getConversation(conversationId)?.let {
                            localChat.updateConversation(it.copy(title = structured.title))
                        }
                    } catch (e: Exception) {
                        // If parsing fails, we use the raw response as message
                    }
                }

                val aiResponse = res.value.toDomain(conversationId).copy(text = responseText)
                localChat.createMessage(aiResponse.toEntity())
                Result.success(aiResponse)
            }

            is RestResult.Failure -> {
                Result.failure(
                    RuntimeException(
                        buildString {
                            append(res.error.type)
                            res.error.message?.let { append(": $it") }
                            res.body?.let { append(" | $it") }
                        }
                    )
                )
            }
        }
    }
}
