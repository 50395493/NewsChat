package com.stealthnews.chat.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.stealthnews.chat.data.local.dao.ChatMessageDao
import com.stealthnews.chat.data.local.dao.ConversationDao
import com.stealthnews.chat.data.local.dao.UserDao
import com.stealthnews.chat.data.local.entity.ChatMessage
import com.stealthnews.chat.data.local.entity.Conversation
import com.stealthnews.chat.data.local.entity.User

@Database(
    entities = [User::class, ChatMessage::class, Conversation::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
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
                    "chat_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
