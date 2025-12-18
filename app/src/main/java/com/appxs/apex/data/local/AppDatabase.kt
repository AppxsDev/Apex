package com.appxs.apex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.appxs.apex.data.datasource.local.dao.ConversationDao
import com.appxs.apex.data.datasource.local.dao.MessageDao
import com.appxs.apex.data.datasource.local.entity.ConversationEntity
import com.appxs.apex.data.datasource.local.entity.MessageEntity

// data/local/AppDatabase.kt
@Database(
    entities = [ConversationEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
}