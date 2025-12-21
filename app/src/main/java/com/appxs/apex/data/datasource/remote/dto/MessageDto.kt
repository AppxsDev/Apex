package com.appxs.apex.data.datasource.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val role: String,
    val content: String,
    val timestamp: Long
)