package com.stealthnews.chat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class News(
    @PrimaryKey
    val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val category: String,
    val source: String,
    val imageUrl: String? = null,
    val publishTime: Long,
    val isSubscribed: Boolean = false,
    val readCount: Int = 0,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)