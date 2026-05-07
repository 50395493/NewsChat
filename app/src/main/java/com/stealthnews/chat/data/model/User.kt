package com.stealthnews.chat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    val phoneNumber: String,
    val nickname: String,
    val avatar: String? = null,
    val status: UserStatus = UserStatus.OFFLINE,
    val lastSeen: Long = System.currentTimeMillis(),
    val isOnline: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    enum class UserStatus {
        ONLINE, OFFLINE, AWAY, BUSY
    }
}