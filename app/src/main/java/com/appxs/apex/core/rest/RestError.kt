package com.appxs.apex.core.rest

data class RestError(
    val type: Type,
    val message: String? = null,
    val cause: Throwable? = null
) {
    enum class Type {
        Network, Timeout, Http, Serialization, Cancelled, Unknown
    }
}