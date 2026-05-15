package com.stealthnews.chat.data.local.dao

import androidx.room.*
import com.stealthnews.chat.data.local.entity.Conversation
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversations ORDER BY lastMessageTime DESC")
    fun getAllConversations(): Flow<List<Conversation>>

    @Query("SELECT * FROM conversations WHERE id = :conversationId")
    suspend fun getConversationById(conversationId: String): Conversation?

    @Query("SELECT * FROM conversations WHERE participantIds LIKE '%' || :participantId || '%' LIMIT 1")
    suspend fun getConversationByParticipant(participantId: String): Conversation?

    @Query("UPDATE conversations SET lastMessageId = :messageId, lastMessageContent = :content, lastMessageTime = :time WHERE id = :conversationId")
    suspend fun updateLastMessage(conversationId: String, messageId: String, content: String, time: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation)

    @Update
    suspend fun updateConversation(conversation: Conversation)

    @Delete
    suspend fun deleteConversation(conversation: Conversation)

    @Query("DELETE FROM conversations")
    suspend fun deleteAllConversations()
}
