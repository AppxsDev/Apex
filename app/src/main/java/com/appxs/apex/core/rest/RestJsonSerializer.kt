package com.appxs.apex.core.rest

interface RestJsonSerializer {
    fun <T : Any> encode(value: T, clazz: Class<T>): String
    fun <T : Any> decode(json: String, clazz: Class<T>): T
}
