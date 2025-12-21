package com.appxs.apex.data.datasource.remote

import com.appxs.apex.core.rest.RestDriver
import com.appxs.apex.core.rest.RestResult
import com.appxs.apex.core.rest.post
import com.appxs.apex.data.config.AIConfig
import com.appxs.apex.data.datasource.remote.dto.AiRequestDto
import com.appxs.apex.data.datasource.remote.dto.AiResponseDto
import com.appxs.apex.data.datasource.remote.dto.MessageDto

class RemoteDataSource(
    private val rest: RestDriver,
    private val aiConfig: AIConfig) {
    
    suspend fun sendMessage(messageDto: MessageDto): RestResult<AiResponseDto> {
        val aiRequest = AiRequestDto(
            messages = listOf(messageDto)
        )

        return rest.post(
            url = "https://api.cloudflare.com/client/v4/accounts/${aiConfig.account}/ai/run/@cf/meta/llama-3-8b-instruct",
            body = aiRequest,
            headers = mapOf(
                "Authorization" to "Bearer ${aiConfig.token}",
                "Content-Type" to "application/json"
            )
        )
    }
}