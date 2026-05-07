package com.stealthnews.chat.data.local.dao

import androidx.room.*
import com.stealthnews.chat.data.model.News
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: News)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNews(newsList: List<News>)
    
    @Update
    suspend fun updateNews(news: News)
    
    @Delete
    suspend fun deleteNews(news: News)
    
    @Query("SELECT * FROM news ORDER BY publishTime DESC")
    fun getAllNews(): Flow<List<News>>
    
    @Query("SELECT * FROM news WHERE category = :category ORDER BY publishTime DESC")
    fun getNewsByCategory(category: String): Flow<List<News>>
    
    @Query("SELECT * FROM news WHERE isSubscribed = 1 ORDER BY publishTime DESC")
    fun getSubscribedNews(): Flow<List<News>>
    
    @Query("SELECT * FROM news WHERE title LIKE '%' || :query || '%' OR summary LIKE '%' || :query || '%' ORDER BY publishTime DESC")
    fun searchNews(query: String): Flow<List<News>>
    
    @Query("SELECT * FROM news WHERE id = :id")
    suspend fun getNewsById(id: String): News?
    
    @Query("SELECT DISTINCT category FROM news")
    suspend fun getCategories(): List<String>
    
    @Query("UPDATE news SET isSubscribed = :isSubscribed WHERE id = :id")
    suspend fun updateSubscription(id: String, isSubscribed: Boolean)
    
    @Query("UPDATE news SET readCount = readCount + 1 WHERE id = :id")
    suspend fun incrementReadCount(id: String)
}