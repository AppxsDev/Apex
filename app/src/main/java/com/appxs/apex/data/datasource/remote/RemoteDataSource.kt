package com.appxs.apex.data.datasource.remote

import com.appxs.apex.core.rest.RestDriver
import com.appxs.apex.core.rest.RestResult
import com.appxs.apex.core.rest.post
import com.appxs.apex.data.config.AIConfig
import com.appxs.apex.data.datasource.remote.dto.MessageDto

class RemoteDataSource(
    private val rest: RestDriver,
    private val aiConfig: AIConfig) {
    
    suspend fun sendMessage(messageDto: MessageDto): RestResult<MessageDto> {
        return rest.post(
            url = "https://api.cloudflare.com/client/v4/accounts/${aiConfig.account}/ai/run/@cf/meta/llama-3-8b-instruct",
            body = messageDto.content,
            headers = mapOf("Authorization" to "Bearer ${aiConfig.token}")
        )
    }
}