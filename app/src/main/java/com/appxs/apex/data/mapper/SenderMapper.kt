package com.appxs.apex.data.mapper

import com.appxs.apex.domain.model.Sender

fun Sender.toRemote(): String =
    when (this) {
        Sender.User -> "user"
        Sender.Ai -> "system"
    }

fun String.toSender(): Sender =
    when (lowercase()) {
        "user" -> Sender.User
        "system" -> Sender.Ai
        else -> Sender.User
    }