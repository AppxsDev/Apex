package com.appxs.apex.data.datasource.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StructuredAiResponseDto(
    val title: String,
    val message: String
)