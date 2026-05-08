package com.stealthnews.chat.data.model

data class NewsItem(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val source: String,
    val publishedAt: Long,
    val category: String = "general",
    val url: String? = null
)
