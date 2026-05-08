package com.stealthnews.chat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    val username: String,
    val displayName: String,
    val avatar: String? = null,
    val email: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
