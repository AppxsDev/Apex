package com.appxs.apex.data.datasource.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AiResponseDto(
    val result: ResultDto,
    val success: Boolean,
    val errors: List<AiErrorDto> = emptyList(),
    val messages: List<String> = emptyList()
)

@Serializable
data class ResultDto(
    val response: String,
    val usage: UsageDto? = null
)

@Serializable
data class UsageDto(
    val total_tokens: Int
)

@Serializable
data class AiErrorDto(
    val code: Int? = null,
    val message: String? = null
)