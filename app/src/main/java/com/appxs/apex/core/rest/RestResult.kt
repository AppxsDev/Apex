package com.appxs.apex.core.rest

sealed interface RestResult<out T> {
    data class Success<T>(
        val value: T,
        val statusCode: Int,
        val headers: Map<String, String>
    ) : RestResult<T>

    data class Failure(
        val error: RestError,
        val statusCode: Int? = null,
        val body: String? = null
    ) : RestResult<Nothing>
}