package com.appxs.apex.core.rest

import io.ktor.util.reflect.TypeInfo

interface RestDriver {
    suspend fun <Res : Any> execute(
        request: Request,
        responseType: TypeInfo
    ): RestResult<Res>

    suspend fun <Req : Any, Res : Any> post(
        url: String,
        body: Req,
        responseType: TypeInfo,
        headers: Map<String, String> = emptyMap(),
        query: Map<String, String> = emptyMap(),
    ): RestResult<Res>
}