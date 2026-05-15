package com.stealthnews.chat.data.local.dao

import androidx.room.*
import com.stealthnews.chat.data.model.Friend
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendDao {
    
    @Query("SELECT * FROM friends WHERE userId = :userId AND status = 'ACCEPTED' ORDER BY nickname ASC")
    fun getFriends(userId: String): Flow<List<Friend>>
    
    @Query("SELECT * FROM friends WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllFriendRequests(userId: String): Flow<List<Friend>>
    
    @Query("SELECT * FROM friends WHERE userId = :userId AND status = 'PENDING' ORDER BY createdAt DESC")
    fun getPendingRequests(userId: String): Flow<List<Friend>>
    
    @Query("SELECT * FROM friends WHERE id = :friendId")
    suspend fun getFriendById(friendId: String): Friend?
    
    @Query("SELECT * FROM friends WHERE id = :friendId AND userId = :userId")
    suspend fun getFriendByIdAndUser(friendId: String, userId: String): Friend?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriend(friend: Friend)
    
    @Update
    suspend fun updateFriend(friend: Friend)
    
    @Delete
    suspend fun deleteFriend(friend: Friend)
    
    @Query("DELETE FROM friends WHERE id = :friendId AND userId = :userId")
    suspend fun deleteFriendById(friendId: String, userId: String)
    
    @Query("UPDATE friends SET status = :status WHERE id = :friendId")
    suspend fun updateFriendStatus(friendId: String, status: Friend.FriendStatus)
    
    @Query("UPDATE friends SET isOnline = :isOnline WHERE id = :friendId")
    suspend fun updateOnlineStatus(friendId: String, isOnline: Boolean)
    
    @Query("SELECT COUNT(*) FROM friends WHERE userId = :userId AND status = 'ACCEPTED'")
    suspend fun getFriendCount(userId: String): Int
    
    @Query("SELECT COUNT(*) FROM friends WHERE userId = :userId AND status = 'PENDING'")
    suspend fun getPendingRequestCount(userId: String): Int
    
    @Query("SELECT * FROM friends WHERE userId = :userId AND (nickname LIKE '%' || :keyword || '%' OR id LIKE '%' || :keyword || '%')")
    fun searchFriends(userId: String, keyword: String): Flow<List<Friend>>
}
