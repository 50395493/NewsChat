package com.stealthnews.chat.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.stealthnews.chat.ui.news.NewsActivity
import com.stealthnews.chat.util.PreferenceManager

class ScreenStateReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "ScreenStateReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val preferenceManager = PreferenceManager(context)

        when (intent.action) {
            Intent.ACTION_SCREEN_OFF -> {
                Log.d(TAG, "Screen turned OFF")
                // 屏幕关闭时如果聊天已激活则自动锁定
                if (preferenceManager.isChatActivated() && preferenceManager.shouldAutoLock()) {
                    // 重置聊天激活状态
                    preferenceManager.setChatActivated(false)
                }
            }
            Intent.ACTION_USER_PRESENT -> {
                Log.d(TAG, "Screen unlocked")
                // 用户解锁屏幕，可以检查是否需要重新认证
            }
        }
    }
}
