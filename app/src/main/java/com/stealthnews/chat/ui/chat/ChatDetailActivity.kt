package com.stealthnews.chat.ui.chat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.stealthnews.chat.R
import com.stealthnews.chat.data.local.database.AppDatabase
import com.stealthnews.chat.data.local.entity.ChatMessage
import com.stealthnews.chat.databinding.ActivityChatDetailBinding
import com.stealthnews.chat.util.PreferenceManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

class ChatDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatDetailBinding
    private lateinit var database: AppDatabase
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var chatAdapter: ChatAdapter
    
    private var conversationId: String = ""
    private var title: String = ""
    private var currentUserId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        database = AppDatabase.getInstance(this)
        preferenceManager = PreferenceManager(this)
        
        conversationId = intent.getStringExtra("conversation_id") ?: ""
        title = intent.getStringExtra("title") ?: ""
        currentUserId = preferenceManager.getUserId() ?: ""
        
        setupViews()
        loadMessages()
    }

    private fun setupViews() {
        binding.tvFriendName.text = title
        binding.tvFriendStatus.text = getString(R.string.online)
        
        binding.btnBack.setOnClickListener {
            finish()
        }
        
        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
            }
        }
        
        chatAdapter = ChatAdapter(currentUserId)
        binding.rvMessages.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        binding.rvMessages.adapter = chatAdapter
    }

    private fun loadMessages() {
        lifecycleScope.launch {
            database.chatMessageDao().getMessagesByConversation(conversationId).collectLatest { messages ->
                chatAdapter.submitList(messages) {
                    if (messages.isNotEmpty()) {
                        binding.rvMessages.scrollToPosition(messages.size - 1)
                    }
                }
            }
        }
    }

    private fun sendMessage(content: String) {
        lifecycleScope.launch {
            val message = ChatMessage(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                senderId = currentUserId,
                receiverId = "", // 单聊不需要
                content = content,
                messageType = "text",
                timestamp = System.currentTimeMillis()
            )
            
            database.chatMessageDao().insertMessage(message)
            
            // 更新会话最后消息
            val conversation = database.conversationDao().getConversationById(conversationId)
            conversation?.let {
                database.conversationDao().updateLastMessage(
                    conversationId,
                    message.id,
                    content,
                    System.currentTimeMillis()
                )
            }
            
            binding.etMessage.text?.clear()
            
            // 模拟自动回复
            simulateReply()
        }
    }

    private fun simulateReply() {
        lifecycleScope.launch {
            kotlinx.coroutines.delay(1500)
            val replyMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                senderId = "friend",
                receiverId = currentUserId,
                content = getRandomReply(),
                messageType = "text",
                timestamp = System.currentTimeMillis()
            )
            database.chatMessageDao().insertMessage(replyMessage)
        }
    }

    private fun getRandomReply(): String {
        val replies = listOf(
            "好的，我知道了",
            "哈哈，这个有意思",
            "确实是这样",
            "让我想想...",
            "收到！",
            "这个主意不错",
            "有道理",
            "我也这么觉得"
        )
        return replies.random()
    }
}
