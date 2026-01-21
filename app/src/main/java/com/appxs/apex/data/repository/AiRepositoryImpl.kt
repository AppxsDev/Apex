package com.appxs.apex.data.repository

import com.appxs.apex.core.rest.RestResult
import com.appxs.apex.core.time.SecureTimeDataSource
import com.appxs.apex.data.datasource.local.LocalDataSource
import com.appxs.apex.data.datasource.remote.RemoteDataSource
import com.appxs.apex.data.datasource.remote.dto.MessageDto
import com.appxs.apex.data.mapper.toDomain
import com.appxs.apex.data.mapper.toEntity
import com.appxs.apex.data.mapper.toRemote
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender
import com.appxs.apex.domain.repository.AiRepository

class AiRepositoryImpl(
    private val remoteChat: RemoteDataSource,
    private val localChat: LocalDataSource,
    private val secureTime: SecureTimeDataSource): AiRepository {

    override suspend fun sendMessage(text: String, conversationId: Long, history: List<Message>): Result<Message> {
        val currentTime = secureTime.getCurrentTimeInMillis()
        
        val systemMessage = MessageDto(
            role = "system",
            content = "Your name is Apex. You must answer clearly as much as possible. You must answer in the same language of the user. You are a helpful assistant.",
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
                val aiResponse = res.value.toDomain(conversationId)
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
