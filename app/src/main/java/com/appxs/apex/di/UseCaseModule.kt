package com.appxs.apex.di

import com.appxs.apex.domain.repository.AiRepository
import com.appxs.apex.domain.repository.ChatRepository
import com.appxs.apex.domain.usecase.ai.SendMessageToAiUseCase
import com.appxs.apex.domain.usecase.chat.CreateConversationUseCase
import com.appxs.apex.domain.usecase.chat.DeleteConversationUseCase
import com.appxs.apex.domain.usecase.chat.GetConversationsUseCase
import com.appxs.apex.domain.usecase.chat.GetMessagesUseCase
import com.appxs.apex.domain.usecase.chat.SendMessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetConversationsUseCase(repo: ChatRepository) = GetConversationsUseCase(repo)

    @Provides
    fun provideGetMessagesUseCase(repo: ChatRepository) = GetMessagesUseCase(repo)

    @Provides
    fun provideCreateConversationUseCase(repo: ChatRepository) = CreateConversationUseCase(repo)

    @Provides
    fun provideDeleteConversationUseCase(repo: ChatRepository) = DeleteConversationUseCase(repo)

    @Provides
    fun provideSendMessageUseCase(repo: ChatRepository) = SendMessageUseCase(repo)

    @Provides
    fun provideSendMessageToAiUseCase(repo: AiRepository) = SendMessageToAiUseCase(repo)
}