package com.appxs.apex.di

import com.appxs.apex.core.rest.RestDriver
import com.appxs.apex.core.time.SecureTimeDataSource
import com.appxs.apex.data.config.AIConfig
import com.appxs.apex.data.datasource.local.LocalDataSource
import com.appxs.apex.data.datasource.local.dao.ConversationDao
import com.appxs.apex.data.datasource.local.dao.MessageDao
import com.appxs.apex.data.datasource.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideLocalDataSource(
        conversationDao: ConversationDao,
        messageDao: MessageDao
    ): LocalDataSource = LocalDataSource(
        conversationDao,
        messageDao)

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        rest: RestDriver,
        aiConfig: AIConfig
    ): RemoteDataSource = RemoteDataSource(
        rest,
        aiConfig)

    @Provides
    @Singleton
    fun provideSecureTimeDataSource(): SecureTimeDataSource = SecureTimeDataSource()
}