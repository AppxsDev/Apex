package com.appxs.apex.di

import com.appxs.apex.core.time.SecureTimeDataSource
import com.appxs.apex.data.datasource.local.LocalDataSource
import com.appxs.apex.data.datasource.remote.RemoteDataSource
import com.appxs.apex.data.repository.AiRepositoryImpl
import com.appxs.apex.data.repository.ChatRepositoryImpl
import com.appxs.apex.domain.repository.AiRepository
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
        localDataSource: LocalDataSource,
        secureTimeDataSource: SecureTimeDataSource
    ): ChatRepository = ChatRepositoryImpl(
        localDataSource,
        secureTimeDataSource)

    @Provides
    @Singleton
    fun provideAiRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        secureTimeDataSource: SecureTimeDataSource
    ): AiRepository = AiRepositoryImpl(
        remoteDataSource,
        localDataSource,
        secureTimeDataSource
    )
}