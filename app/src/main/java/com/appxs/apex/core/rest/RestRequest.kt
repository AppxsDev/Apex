package com.appxs.apex.core.rest

data class Request(
    val method: HttpMethod = HttpMethod.POST,
    val url: String,
    val headers: Map<String, String> = emptyMap(),
    val query: Map<String, String> = emptyMap(),
    val body: Any? = null
)

enum class HttpMethod { GET, POST, PUT, PATCH, DELETE }
