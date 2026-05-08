package com.stealthnews.chat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey
    val id: String,
    val conversationId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val messageType: String = "text",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isSent: Boolean = true
)
