package com.stealthnews.chat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class Conversation(
    @PrimaryKey
    val id: String,
    val type: ConversationType,
    val title: String,
    val participantIds: List<String>,
    val lastMessageId: String? = null,
    val lastMessageContent: String? = null,
    val lastMessageTime: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0,
    val isMuted: Boolean = false,
    val isArchived: Boolean = false,
    val isEncrypted: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    enum class ConversationType {
        PRIVATE, GROUP
    }
}