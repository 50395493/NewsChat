package com.stealthnews.chat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends")
data class Friend(
    @PrimaryKey
    val id: String,
    val nickname: String,
    val avatarUrl: String? = null,
    val userId: String, // The user who added this friend
    val status: FriendStatus = FriendStatus.PENDING,
    val isOnline: Boolean = false,
    val lastSeen: Long = System.currentTimeMillis(),
    val note: String? = null, // 备注名
    val createdAt: Long = System.currentTimeMillis()
) {
    enum class FriendStatus {
        PENDING,    // 待确认
        ACCEPTED,   // 已接受
        REJECTED,   // 已拒绝
        BLOCKED     // 已拉黑
    }
}
