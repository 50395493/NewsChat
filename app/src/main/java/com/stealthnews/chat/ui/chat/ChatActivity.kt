package com.stealthnews.chat.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.stealthnews.chat.data.local.database.AppDatabase
import com.stealthnews.chat.data.local.dao.ChatMessageDao
import com.stealthnews.chat.databinding.ActivityChatBinding
import com.stealthnews.chat.util.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatMessageDao: ChatMessageDao
    private lateinit var adapter: ChatAdapter
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)
        val database = AppDatabase.getInstance(this)
        chatMessageDao = database.chatMessageDao()

        setupToolbar()
        setupRecyclerView()
        setupListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        val currentUserId = preferenceManager.getUserId() ?: ""
        adapter = ChatAdapter(currentUserId)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
            }
        }
    }

    private fun sendMessage(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            // TODO: 发送消息逻辑
            withContext(Dispatchers.Main) {
                binding.etMessage.text?.clear()
            }
        }
    }
}
