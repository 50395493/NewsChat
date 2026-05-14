package com.stealthnews.chat.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stealthnews.chat.databinding.ActivitySettingsBinding
import com.stealthnews.chat.ui.auth.LoginActivity
import com.stealthnews.chat.util.PreferenceManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)
        setupViews()
        loadUserInfo()
    }

    private fun setupViews() {
        // 返回按钮
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // 夜间模式开关
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, if (isChecked) "已开启夜间模式" else "已关闭夜间模式", Toast.LENGTH_SHORT).show()
        }

        // 消息推送开关
        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, if (isChecked) "已开启消息推送" else "已关闭消息推送", Toast.LENGTH_SHORT).show()
        }

        // 清除缓存
        binding.layoutClearCache.setOnClickListener {
            Toast.makeText(this, "缓存已清除", Toast.LENGTH_SHORT).show()
            binding.tvCacheSize.text = "0 B"
        }

        // 隐私政策
        binding.layoutPrivacy.setOnClickListener {
            Toast.makeText(this, "查看隐私政策", Toast.LENGTH_SHORT).show()
        }

        // 版本信息
        binding.tvVersion.text = "1.0.0"

        // 退出登录
        binding.btnLogout.setOnClickListener {
            preferenceManager.logout()
            Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }
    }

    private fun loadUserInfo() {
        val userId = preferenceManager.getUserId()
        val phone = preferenceManager.getPhoneNumber()

        binding.tvUsername.text = "用户"
        binding.tvUserPhone.text = if (!phone.isNullOrEmpty() && phone.length > 7) {
            "${phone.substring(0, 3)}****${phone.substring(phone.length - 4)}"
        } else {
            phone ?: "未登录"
        }
    }
}
