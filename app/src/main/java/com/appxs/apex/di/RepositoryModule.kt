package com.appxs.apex.di

import com.appxs.apex.core.time.SecureTimeDataSource
import com.appxs.apex.data.datasource.local.LocalChatDataSource
import com.appxs.apex.data.repository.ChatRepositoryImpl
import com.appxs.apex.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideChatRepository(
        localChatDataSource: LocalChatDataSource,
        secureTimeDataSource: SecureTimeDataSource
    ): ChatRepository = ChatRepositoryImpl(
        localChatDataSource,
        secureTimeDataSource)
}