package com.appxs.apex.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.appxs.apex.data.datasource.local.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    @Insert
    suspend fun insert(entity: ConversationEntity) : Long

    @Query("SELECT * FROM conversations ORDER BY lastMessageAt DESC")
    fun getAll(): Flow<List<ConversationEntity>>

    @Update
    suspend fun update(entity: ConversationEntity)

    @Delete
    suspend fun delete(entity: ConversationEntity)
}
