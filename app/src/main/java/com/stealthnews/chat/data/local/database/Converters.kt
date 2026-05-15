package com.stealthnews.chat.data.local.database

import androidx.room.TypeConverter
import com.stealthnews.chat.data.model.Friend
import com.stealthnews.chat.data.model.MessageType

class Converters {
    
    @TypeConverter
    fun fromMessageType(value: MessageType): String = value.name
    
    @TypeConverter
    fun toMessageType(value: String): MessageType = MessageType.valueOf(value)
    
    @TypeConverter
    fun fromFriendStatus(value: Friend.FriendStatus): String = value.name
    
    @TypeConverter
    fun toFriendStatus(value: String): Friend.FriendStatus = Friend.FriendStatus.valueOf(value)
    
    @TypeConverter
    fun fromStringList(value: List<String>): String = value.joinToString(",")
    
    @TypeConverter
    fun toStringList(value: String): List<String> = 
        if (value.isEmpty()) emptyList() else value.split(",")
}
