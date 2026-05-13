package com.stealthnews.chat.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.stealthnews.chat.R
import com.stealthnews.chat.util.PreferenceManager

class NotificationService : Service() {

    private lateinit var preferenceManager: PreferenceManager

    companion object {
        const val CHANNEL_ID_NEWS = "news_channel"
        const val CHANNEL_ID_CHAT = "chat_channel"
        const val ACTION_SHOW_NOTIFICATION = "com.stealthnews.SHOW_NOTIFICATION"
        const val EXTRA_TITLE = "title"
        const val EXTRA_CONTENT = "content"
        const val EXTRA_TYPE = "type"
    }

    override fun onCreate() {
        super.onCreate()
        preferenceManager = PreferenceManager(this)
        createNotificationChannels()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SHOW_NOTIFICATION -> {
                val title = intent.getStringExtra(EXTRA_TITLE) ?: return START_NOT_STICKY
                val content = intent.getStringExtra(EXTRA_CONTENT) ?: return START_NOT_STICKY
                val type = intent.getStringExtra(EXTRA_TYPE) ?: "news"
                showNotification(title, content, type)
            }
        }
        return START_NOT_STICKY
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val newsChannel = NotificationChannel(
                CHANNEL_ID_NEWS,
                getString(R.string.notification_channel_news),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "新闻推送通知"
            }

            val chatChannel = NotificationChannel(
                CHANNEL_ID_CHAT,
                getString(R.string.notification_channel_chat),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "私密聊天通知"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(newsChannel)
            manager.createNotificationChannel(chatChannel)
        }
    }

    private fun showNotification(title: String, content: String, type: String) {
        val channelId = if (type == "chat") CHANNEL_ID_CHAT else CHANNEL_ID_NEWS

        // 根据伪装设置修改通知内容
        val (displayTitle, displayContent) = when (preferenceManager.getNotificationType()) {
            PreferenceManager.NotificationType.RANDOM_NEWS -> {
                Pair(getString(R.string.notification_title_news), content)
            }
            PreferenceManager.NotificationType.CUSTOM_TEXT -> {
                Pair(title, preferenceManager.getCustomNotification())
            }
            PreferenceManager.NotificationType.RANDOM_CONTENT -> {
                Pair(getString(R.string.notification_title_news), content)
            }
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_news)
            .setContentTitle(displayTitle)
            .setContentText(displayContent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
