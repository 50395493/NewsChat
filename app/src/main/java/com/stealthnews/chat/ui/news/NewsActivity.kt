package com.stealthnews.chat.ui.news

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.stealthnews.chat.R
import com.stealthnews.chat.databinding.ActivityNewsBinding
import com.stealthnews.chat.ui.chat.ChatActivity
import com.stealthnews.chat.ui.login.LoginActivity
import com.stealthnews.chat.ui.settings.SettingsActivity

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        
        setupToolbar()
        setupViewPager()
        setupSwipeRefresh()
        setupBottomNavigation()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                R.id.action_chat -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupViewPager() {
        val adapter = NewsPagerAdapter(this)
        binding.viewPager.adapter = adapter
        
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Tech"
                1 -> "Sports"
                2 -> "Entertainment"
                else -> ""
            }
        }.attach()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadNews()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_news -> true
                R.id.nav_chat -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.news.observe(this) { newsList ->
            // 更新新闻列表
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }
    }
}
