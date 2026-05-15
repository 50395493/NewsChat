package com.stealthnews.chat.ui.chat

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.stealthnews.chat.R
import com.stealthnews.chat.databinding.ActivityStealthSettingsBinding
import com.stealthnews.chat.util.SecurityManager
import com.stealthnews.chat.data.local.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StealthSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStealthSettingsBinding
    private lateinit var securityManager: SecurityManager
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStealthSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        securityManager = SecurityManager(this)
        database = AppDatabase.getInstance(this)
        
        setupToolbar()
        loadSettings()
        setupClickListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun loadSettings() {
        // 显示当前激活码（隐藏部分）
        val currentCode = securityManager.getCurrentActivationCode()
        binding.tvCurrentCode.text = if (currentCode.length > 4) {
            "••••" + currentCode.takeLast(4)
        } else {
            "••••••"
        }
    }

    private fun setupClickListeners() {
        // 修改激活码
        binding.layoutChangeCode.setOnClickListener {
            showChangeActivationCodeDialog()
        }
        
        // 已读回执
        binding.switchReadReceipts.setOnCheckedChangeListener { _, isChecked ->
            // 保存设置
        }
        
        // 在线状态
        binding.switchOnlineStatus.setOnCheckedChangeListener { _, isChecked ->
            // 保存设置
        }
        
        // 最后上线
        binding.switchLastSeen.setOnCheckedChangeListener { _, isChecked ->
            // 保存设置
        }
        
        // 消息通知
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            // 保存设置
        }
        
        // 隐蔽通知
        binding.switchStealthNotifications.setOnCheckedChangeListener { _, isChecked ->
            // 保存设置
        }
        
        // 清除聊天记录
        binding.layoutClearHistory.setOnClickListener {
            showClearHistoryDialog()
        }
    }

    private fun showChangeActivationCodeDialog() {
        val editText = EditText(this).apply {
            hint = getString(R.string.enter_new_code)
            setPadding(48, 32, 48, 32)
        }
        
        AlertDialog.Builder(this)
            .setTitle(R.string.change_activation_code)
            .setView(editText)
            .setPositiveButton(R.string.save) { _, _ ->
                val newCode = editText.text.toString().trim()
                if (newCode.isNotEmpty() && newCode.length >= 4) {
                    securityManager.setActivationCode(newCode)
                    Toast.makeText(this, R.string.code_changed, Toast.LENGTH_SHORT).show()
                    loadSettings()
                } else {
                    Toast.makeText(this, "激活码至少4位", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showClearHistoryDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.clear_chat_history)
            .setMessage("确定要清除所有聊天记录吗？此操作不可恢复。")
            .setPositiveButton(R.string.confirm) { _, _ ->
                clearChatHistory()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun clearChatHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            database.chatMessageDao().deleteAllMessages()
            database.conversationDao().deleteAllConversations()
            runOnUiThread {
                Toast.makeText(this@StealthSettingsActivity, R.string.history_cleared, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
