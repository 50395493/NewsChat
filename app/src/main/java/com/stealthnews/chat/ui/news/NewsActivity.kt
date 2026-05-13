package com.stealthnews.chat.ui.news

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.stealthnews.chat.R
import com.stealthnews.chat.databinding.ActivityNewsBinding
import com.stealthnews.chat.ui.auth.LoginActivity
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
        
        // Setup search functionality (used for chat activation)
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (securityManager.checkActivationCode(it)) {
                        activateChatMode()
                        return true
                    }
                }
                return false
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        
        // Setup ViewPager with adapter
        val adapter = NewsPagerAdapter(this)
        binding.viewPager.adapter = adapter
        
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.news_tab_tech)
                1 -> getString(R.string.news_tab_sports)
                2 -> getString(R.string.news_tab_entertainment)
                else -> ""
            }
        }.attach()
        
        // Setup refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadNews()
            binding.swipeRefresh.isRefreshing = false
        }
        
        // Setup settings button
        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        
        // Setup triple click detection for chat content reveal
        setupTripleClickDetection()
    }
    
    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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
    
    private fun setupTripleClickDetection() {
        var clickCount = 0
        var lastClickTime = 0L
        
        binding.root.setOnClickListener {
            if (isChatMode) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime < 500) {
                    clickCount++
                } else {
                    clickCount = 1
                }
                lastClickTime = currentTime
                
                if (clickCount >= 3) {
                    revealChatContent()
                    clickCount = 0
                }
            }
        }
    }
    
    private fun activateChatMode() {
        isChatMode = true
        binding.tabLayout.visibility = View.GONE
        binding.viewPager.visibility = View.GONE
        showChatInterface()
        autoLockTimer?.cancel()
        setupAutoLock()
    }
    
    private fun deactivateChatMode() {
        isChatMode = false
        binding.tabLayout.visibility = View.VISIBLE
        binding.viewPager.visibility = View.VISIBLE
        hideChatInterface()
        binding.searchView.setQuery("", false)
    }
    
    private fun showChatInterface() {
        // TODO: Implement chat interface loading
    }
    
    private fun hideChatInterface() {
        // TODO: Implement chat interface hiding
    }
    
    private fun revealChatContent() {
        // TODO: Implement chat content reveal logic
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
