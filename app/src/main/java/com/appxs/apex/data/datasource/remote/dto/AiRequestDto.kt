package com.appxs.apex.data.datasource.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AiRequestDto(
    val messages: List<MessageDto>
)