package com.appxs.apex.core.rest

import io.ktor.util.reflect.typeInfo

@OptIn(ExperimentalStdlibApi::class)
suspend inline fun <reified Res : Any> RestDriver.execute(
    request: Request
): RestResult<Res> =
    execute(request, typeInfo<Res>())

@OptIn(ExperimentalStdlibApi::class)
suspend inline fun <reified Req : Any, reified Res : Any> RestDriver.post(
    url: String,
    body: Req,
    headers: Map<String, String> = emptyMap(),
    query: Map<String, String> = emptyMap(),
): RestResult<Res> =
    post(
        url = url,
        body = body,
        responseType = typeInfo<Res>(),
        headers = headers,
        query = query
    )