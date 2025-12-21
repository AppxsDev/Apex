package com.appxs.apex.core.rest.impl

import com.appxs.apex.core.rest.RestError
import com.appxs.apex.core.rest.HttpMethod
import com.appxs.apex.core.rest.Request
import com.appxs.apex.core.rest.RestDriver
import com.appxs.apex.core.rest.RestResult
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.reflect.TypeInfo
import kotlinx.coroutines.CancellationException

class KtorRestDriver(
    private val client: HttpClient
) : RestDriver {

    override suspend fun <Res : Any> execute(
        request: Request,
        responseType: TypeInfo
    ): RestResult<Res> {
        return try {
            val response = client.request(request.url) {
                method = request.method.toKtor()

                request.headers.forEach { (k, v) -> header(k, v) }

                url {
                    request.query.forEach { (k, v) -> parameters.append(k, v) }
                }

                if (request.method != HttpMethod.GET && request.body != null) {
                    contentType(ContentType.Application.Json)
                    setBody(request.body)
                }
            }

            val status = response.status.value
            val headers = response.headers.toMapSingle()

            if (status in 200..299) {
                val parsed: Res = response.body(responseType)
                RestResult.Success(parsed, status, headers)
            } else {
                RestResult.Failure(
                    error = RestError(RestError.Type.Http, "HTTP $status"),
                    statusCode = status,
                    body = response.bodyAsText()
                )
            }
        } catch (t: CancellationException) {
            RestResult.Failure(RestError(RestError.Type.Cancelled, t.message, t))
        } catch (t: Throwable) {
            RestResult.Failure(RestError(RestError.Type.Unknown, t.message, t))
        }
    }

    override suspend fun <Req : Any, Res : Any> post(
        url: String,
        body: Req,
        responseType: TypeInfo,
        headers: Map<String, String>,
        query: Map<String, String>
    ): RestResult<Res> =
        execute(
            Request(
                method = HttpMethod.POST,
                url = url,
                headers = headers,
                query = query,
                body = body
            ),
            responseType
        )

    private fun HttpMethod.toKtor(): io.ktor.http.HttpMethod = when (this) {
        HttpMethod.GET -> io.ktor.http.HttpMethod.Get
        HttpMethod.POST -> io.ktor.http.HttpMethod.Post
        HttpMethod.PUT -> io.ktor.http.HttpMethod.Put
        HttpMethod.PATCH -> io.ktor.http.HttpMethod.Patch
        HttpMethod.DELETE -> io.ktor.http.HttpMethod.Delete
    }

    private fun Headers.toMapSingle(): Map<String, String> =
        entries().associate { (k, v) -> k to (v.firstOrNull().orEmpty()) }
}
