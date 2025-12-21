package com.appxs.apex.data.repository

import com.appxs.apex.core.rest.RestResult
import com.appxs.apex.core.time.SecureTimeDataSource
import com.appxs.apex.data.datasource.local.LocalDataSource
import com.appxs.apex.data.datasource.remote.RemoteDataSource
import com.appxs.apex.data.datasource.remote.dto.MessageDto
import com.appxs.apex.data.mapper.toDomain
import com.appxs.apex.data.mapper.toEntity
import com.appxs.apex.domain.model.Message
import com.appxs.apex.domain.model.Sender
import com.appxs.apex.domain.repository.AiRepository

class AiRepositoryImpl(
    private val remoteChat: RemoteDataSource,
    private val localChat: LocalDataSource,
    private val secureTime: SecureTimeDataSource): AiRepository {

    override suspend fun sendMessage(text: String, conversationId: Long): Result<Message> {
        val userMessage = MessageDto(
            role = Sender.User.toString(),
            content = text,
            timestamp = secureTime.getCurrentTimeInMillis()
        )

        return when (val res = remoteChat.sendMessage(userMessage)) {
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