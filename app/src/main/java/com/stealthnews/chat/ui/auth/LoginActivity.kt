package com.stealthnews.chat.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stealthnews.chat.R
import com.stealthnews.chat.databinding.ActivityLoginBinding
import com.stealthnews.chat.ui.news.NewsActivity
import com.stealthnews.chat.util.PreferenceManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.btnLogin.setOnClickListener {
            val phone = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_invalid_phone), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, getString(R.string.error_password_length), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 模拟登录成功，保存状态
            PreferenceManager(this).setLoggedIn("user_001", phone, "用户")
            Toast.makeText(this, getString(R.string.success_login), Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, NewsActivity::class.java))
            finish()
        }

        binding.tvGoToRegister.setOnClickListener {
            // 跳转到注册页（可选实现）
        }
    }
}
