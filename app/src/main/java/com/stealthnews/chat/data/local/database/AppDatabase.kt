package com.stealthnews.chat.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.stealthnews.chat.data.model.User
import com.stealthnews.chat.data.model.News
import com.stealthnews.chat.data.model.ChatMessage
import com.stealthnews.chat.data.model.Conversation
import com.stealthnews.chat.data.local.dao.UserDao
import com.stealthnews.chat.data.local.dao.NewsDao
import com.stealthnews.chat.data.local.dao.ChatMessageDao
import com.stealthnews.chat.data.local.dao.ConversationDao

@Database(
    entities = [
        User::class,
        News::class,
        ChatMessage::class,
        Conversation::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun newsDao(): NewsDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun conversationDao(): ConversationDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "stealth_news_chat.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}