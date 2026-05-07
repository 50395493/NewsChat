package com.stealthnews.chat.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(private val context: Context) {
    
    companion object {
        private const val PREF_NAME = "stealth_news_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_PHONE_NUMBER = "phone_number"
        private const val KEY_NICKNAME = "nickname"
        private const val KEY_AUTO_LOCK_TIME = "auto_lock_time"
        private const val KEY_NOTIFICATION_TYPE = "notification_type"
        private const val KEY_CUSTOM_NOTIFICATION = "custom_notification"
        private const val KEY_LAST_ACTIVITY_TIME = "last_activity_time"
        private const val KEY_CHAT_ACTIVATED = "chat_activated"
        private const val DEFAULT_AUTO_LOCK_TIME = 30 // 30秒
    }
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    enum class NotificationType {
        RANDOM_NEWS,      // 随机新闻通知
        CUSTOM_TEXT,      // 自定义文本
        RANDOM_CONTENT    // 随机内容
    }
    
    // 用户认证相关
    fun setLoggedIn(userId: String, phoneNumber: String, nickname: String) {
        with(sharedPreferences.edit()) {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_ID, userId)
            putString(KEY_PHONE_NUMBER, phoneNumber)
            putString(KEY_NICKNAME, nickname)
            apply()
        }
    }
    
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }
    
    fun getPhoneNumber(): String? {
        return sharedPreferences.getString(KEY_PHONE_NUMBER, null)
    }
    
    fun getNickname(): String? {
        return sharedPreferences.getString(KEY_NICKNAME, null)
    }
    
    fun logout() {
        with(sharedPreferences.edit()) {
            putBoolean(KEY_IS_LOGGED_IN, false)
            remove(KEY_USER_ID)
            remove(KEY_PHONE_NUMBER)
            remove(KEY_NICKNAME)
            apply()
        }
    }
    
    // 伪装设置相关
    fun setAutoLockTime(seconds: Int) {
        sharedPreferences.edit().putInt(KEY_AUTO_LOCK_TIME, seconds).apply()
    }
    
    fun getAutoLockTime(): Int {
        return sharedPreferences.getInt(KEY_AUTO_LOCK_TIME, DEFAULT_AUTO_LOCK_TIME)
    }
    
    fun setNotificationType(type: NotificationType) {
        sharedPreferences.edit().putString(KEY_NOTIFICATION_TYPE, type.name).apply()
    }
    
    fun getNotificationType(): NotificationType {
        val typeName = sharedPreferences.getString(KEY_NOTIFICATION_TYPE, NotificationType.RANDOM_NEWS.name)
        return NotificationType.valueOf(typeName ?: NotificationType.RANDOM_NEWS.name)
    }
    
    fun setCustomNotification(text: String) {
        sharedPreferences.edit().putString(KEY_CUSTOM_NOTIFICATION, text).apply()
    }
    
    fun getCustomNotification(): String {
        return sharedPreferences.getString(KEY_CUSTOM_NOTIFICATION, "您有一条新消息") ?: "您有一条新消息"
    }
    
    // 活动状态管理
    fun updateLastActivityTime() {
        sharedPreferences.edit().putLong(KEY_LAST_ACTIVITY_TIME, System.currentTimeMillis()).apply()
    }
    
    fun getLastActivityTime(): Long {
        return sharedPreferences.getLong(KEY_LAST_ACTIVITY_TIME, 0)
    }
    
    fun setChatActivated(activated: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_CHAT_ACTIVATED, activated).apply()
    }
    
    fun isChatActivated(): Boolean {
        return sharedPreferences.getBoolean(KEY_CHAT_ACTIVATED, false)
    }
    
    // 清除所有数据（安全退出）
    fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }
    
    // 检查是否需要自动锁定
    fun shouldAutoLock(): Boolean {
        if (!isChatActivated()) return false
        
        val lockTime = getAutoLockTime()
        val lastActivityTime = getLastActivityTime()
        val currentTime = System.currentTimeMillis()
        
        return (currentTime - lastActivityTime) >= (lockTime * 1000L)
    }
}