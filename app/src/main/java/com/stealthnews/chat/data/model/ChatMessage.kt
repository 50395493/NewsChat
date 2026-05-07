package com.stealthnews.chat.data.model

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
    val messageType: MessageType,
    val mediaUrl: String? = null,
    val mediaThumbnail: String? = null,
    val duration: Long? = null, // For audio/video messages
    val isEncrypted: Boolean = true,
    val encryptionKey: String? = null,
    val isRead: Boolean = false,
    val isDeleted: Boolean = false,
    val isArchived: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    enum class MessageType {
        TEXT, IMAGE, AUDIO, VIDEO, FILE, EMOJI, SYSTEM
    }
}