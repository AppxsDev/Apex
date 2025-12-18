package com.appxs.apex.di

import com.appxs.apex.core.time.SecureTimeDataSource
import com.appxs.apex.data.datasource.local.LocalChatDataSource
import com.appxs.apex.data.datasource.local.dao.ConversationDao
import com.appxs.apex.data.datasource.local.dao.MessageDao
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
    fun provideLocalChatDataSource(
        conversationDao: ConversationDao,
        messageDao: MessageDao
    ): LocalChatDataSource = LocalChatDataSource(
        conversationDao,
        messageDao)

    @Provides
    @Singleton
    fun provideSecureTimeDataSource(): SecureTimeDataSource = SecureTimeDataSource()
}