package com.appxs.apex.domain.usecase.chat

import com.appxs.apex.domain.repository.ChatRepository
import javax.inject.Inject

class MarkMessageAsReadUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(messageId: Long) {
        repository.markMessageAsRead(messageId)
    }
}
