package com.stealthnews.chat.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ChatBackgroundService : Service() {

    companion object {
        private const val TAG = "ChatBackgroundService"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "ChatBackgroundService started")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 后台聊天服务保持运行
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "ChatBackgroundService destroyed")
    }
}
