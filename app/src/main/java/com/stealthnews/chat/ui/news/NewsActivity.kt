package com.stealthnews.chat.ui.news

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.stealthnews.chat.R
import com.stealthnews.chat.databinding.ActivityNewsBinding
import com.stealthnews.chat.ui.auth.LoginActivity
import com.stealthnews.chat.ui.chat.StealthChatActivity
import com.stealthnews.chat.ui.settings.SettingsActivity
import com.stealthnews.chat.util.PreferenceManager
import com.stealthnews.chat.util.SecurityManager

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var securityManager: SecurityManager

    private var autoLockTimer: CountDownTimer? = null
    private var isChatMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)
        securityManager = SecurityManager(this)
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        setupUI()
        setupObservers()
        checkAuthentication()
        setupAutoLock()
    }

    private fun setupUI() {
        // Setup toolbar
        binding.toolbar.title = getString(R.string.app_name)

        // Setup search functionality
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    // 先检查是否是隐蔽聊天激活码
                    if (securityManager.checkActivationCode(it)) {
                        activateChatMode()
                        return true
                    }
                    // 否则执行新闻搜索
                    if (it.isNotEmpty()) {
                        viewModel.searchNews(it)
                        Toast.makeText(this@NewsActivity, "搜索: $it", Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 实时搜索
                if (!newText.isNullOrEmpty() && newText.length >= 2) {
                    viewModel.searchNews(newText)
                }
                return true
            }
        })

        // 搜索框获取焦点时清除聊天激活码检查
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // 搜索框获得焦点，显示搜索提示
            }
        }

        // Setup ViewPager with adapter
        val adapter = NewsPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.news_tab_tech)
                1 -> getString(R.string.news_tab_sports)
                2 -> getString(R.string.news_tab_entertainment)
                else -> ""
            }
        }.attach()

        // Setup refresh
        binding.swipeRefresh.setColorSchemeResources(R.color.news_primary)
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshNews()
            Toast.makeText(this, "正在刷新新闻...", Toast.LENGTH_SHORT).show()
        }

        // Setup settings button
        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (!isLoading) {
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun checkAuthentication() {
        if (!preferenceManager.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupAutoLock() {
        val lockTime = preferenceManager.getAutoLockTime()
        if (lockTime > 0) {
            autoLockTimer = object : CountDownTimer(lockTime * 1000L, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    // Timer running
                }

                override fun onFinish() {
                    if (isChatMode) {
                        deactivateChatMode()
                    }
                }
            }.start()
        }
    }

    private fun activateChatMode() {
        isChatMode = true
        Toast.makeText(this, "隐蔽聊天模式已激活", Toast.LENGTH_SHORT).show()
        binding.tabLayout.visibility = View.GONE
        binding.viewPager.visibility = View.GONE
        showChatInterface()
        autoLockTimer?.cancel()
        setupAutoLock()
    }

    private fun deactivateChatMode() {
        isChatMode = false
        Toast.makeText(this, "隐蔽聊天模式已退出", Toast.LENGTH_SHORT).show()
        binding.tabLayout.visibility = View.VISIBLE
        binding.viewPager.visibility = View.VISIBLE
        hideChatInterface()
        binding.searchView.setQuery("", false)
    }

    private fun showChatInterface() {
        // 打开完整的隐蔽聊天界面
        startActivity(Intent(this, StealthChatActivity::class.java))
    }

    private fun hideChatInterface() {
        // 界面已在onPause中处理
    }

    override fun onResume() {
        super.onResume()
        autoLockTimer?.cancel()
        setupAutoLock()
    }

    override fun onPause() {
        super.onPause()
        if (isChatMode) {
            deactivateChatMode()
        }
        autoLockTimer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        autoLockTimer?.cancel()
    }
}
