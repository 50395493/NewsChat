package com.stealthnews.chat.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.stealthnews.chat.R
import com.stealthnews.chat.data.local.database.AppDatabase
import com.stealthnews.chat.data.local.entity.Conversation
import com.stealthnews.chat.data.model.Friend
import com.stealthnews.chat.databinding.ActivityStealthChatBinding
import com.stealthnews.chat.util.PreferenceManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

class StealthChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStealthChatBinding
    private lateinit var database: AppDatabase
    private lateinit var preferenceManager: PreferenceManager
    
    private lateinit var conversationAdapter: ConversationAdapter
    private lateinit var friendAdapter: FriendAdapter
    
    private var currentTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStealthChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        database = AppDatabase.getInstance(this)
        preferenceManager = PreferenceManager(this)
        
        setupToolbar()
        setupTabs()
        setupAdapters()
        setupClickListeners()
        loadData()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, StealthSettingsActivity::class.java))
        }
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                updateVisibility()
                if (currentTab == 0) {
                    loadConversations()
                } else {
                    loadFriends()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupAdapters() {
        val currentUserId = preferenceManager.getUserId() ?: ""
        conversationAdapter = ConversationAdapter(currentUserId) { conversation ->
            // 从 participantIds 提取对方ID作为标题
            val participants = conversation.participantIds.split(",")
            val friendId = participants.find { it != currentUserId } ?: "未知"
            openChatDetail(conversation.id, friendId)
        }
        
        friendAdapter = FriendAdapter(
            onAddClick = { /* 已在好友列表中 */ },
            onRemoveClick = { friend -> showRemoveFriendDialog(friend) },
            onItemClick = { friend ->
                // 打开与好友的聊天
                openChatByFriendId(friend.id, friend.nickname)
            },
            showRemoveButton = true
        )
        
        binding.rvChatList.layoutManager = LinearLayoutManager(this)
        binding.rvChatList.adapter = conversationAdapter
        
        binding.rvFriendsList.layoutManager = LinearLayoutManager(this)
        binding.rvFriendsList.adapter = friendAdapter
    }

    private fun setupClickListeners() {
        binding.btnAddFriend.setOnClickListener {
            showAddFriendDialog()
        }
        
        binding.btnSearchFriend.setOnClickListener {
            showSearchFriendDialog()
        }
        
        binding.btnSecretMode.setOnClickListener {
            Toast.makeText(this, "隐身模式已开启", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateVisibility() {
        if (currentTab == 0) {
            binding.rvChatList.visibility = View.VISIBLE
            binding.rvFriendsList.visibility = View.GONE
            updateEmptyState(binding.rvChatList.adapter?.itemCount ?: 0, getString(R.string.stealth_no_messages))
        } else {
            binding.rvChatList.visibility = View.GONE
            binding.rvFriendsList.visibility = View.VISIBLE
            updateEmptyState(binding.rvFriendsList.adapter?.itemCount ?: 0, getString(R.string.stealth_no_friends))
        }
    }

    private fun updateEmptyState(itemCount: Int, message: String) {
        if (itemCount == 0) {
            binding.emptyState.visibility = View.VISIBLE
            binding.tvEmptyMessage.text = if (currentTab == 1) {
                getString(R.string.stealth_add_friend_hint)
            } else {
                message
            }
        } else {
            binding.emptyState.visibility = View.GONE
        }
    }

    private fun loadData() {
        loadConversations()
        loadFriends()
        updateVisibility()
    }

    private fun loadConversations() {
        lifecycleScope.launch {
            database.conversationDao().getAllConversations().collectLatest { conversations ->
                // entity.Conversation 没有 type 字段，直接显示所有
                conversationAdapter.submitList(conversations)
                updateEmptyState(conversations.size, getString(R.string.stealth_no_messages))
            }
        }
    }

    private fun loadFriends() {
        val userId = preferenceManager.getUserId() ?: return
        lifecycleScope.launch {
            database.friendDao().getFriends(userId).collectLatest { friends ->
                friendAdapter.submitList(friends)
                updateEmptyState(friends.size, getString(R.string.stealth_no_friends))
            }
        }
    }

    private fun openChatDetail(conversationId: String, title: String) {
        val intent = Intent(this, ChatDetailActivity::class.java).apply {
            putExtra("conversation_id", conversationId)
            putExtra("title", title)
        }
        startActivity(intent)
    }

    private fun openChatByFriendId(friendId: String, friendName: String) {
        lifecycleScope.launch {
            val conversation = database.conversationDao().getConversationByParticipant(friendId)
            if (conversation != null) {
                openChatDetail(conversation.id, friendName)
            } else {
                // 创建新对话
                val newConversationId = UUID.randomUUID().toString()
                val userId = preferenceManager.getUserId() ?: ""
                val newConversation = Conversation(
                    id = newConversationId,
                    participantIds = "$userId,$friendId",
                    lastMessage = null,
                    lastMessageTime = System.currentTimeMillis(),
                    unreadCount = 0
                )
                database.conversationDao().insertConversation(newConversation)
                openChatDetail(newConversationId, friendName)
            }
        }
    }

    private fun showAddFriendDialog() {
        val editText = EditText(this).apply {
            hint = getString(R.string.enter_friend_id)
            setPadding(48, 32, 48, 32)
        }
        
        AlertDialog.Builder(this)
            .setTitle(R.string.add_friend)
            .setView(editText)
            .setPositiveButton(R.string.add) { _, _ ->
                val friendId = editText.text.toString().trim()
                if (friendId.isNotEmpty()) {
                    addFriend(friendId)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showSearchFriendDialog() {
        val editText = EditText(this).apply {
            hint = getString(R.string.enter_friend_id)
            setPadding(48, 32, 48, 32)
        }
        
        AlertDialog.Builder(this)
            .setTitle(R.string.search_friend)
            .setView(editText)
            .setPositiveButton(R.string.add) { _, _ ->
                val friendId = editText.text.toString().trim()
                if (friendId.isNotEmpty()) {
                    addFriend(friendId)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun addFriend(friendId: String) {
        val userId = preferenceManager.getUserId() ?: return
        
        lifecycleScope.launch {
            val existingFriend = database.friendDao().getFriendByIdAndUser(friendId, userId)
            if (existingFriend != null) {
                Toast.makeText(this@StealthChatActivity, R.string.already_friend, Toast.LENGTH_SHORT).show()
                return@launch
            }
            
            // 检查用户是否存在
            val user = database.userDao().getUserById(friendId)
            val nickname = user?.nickname ?: "用户${friendId.take(6)}"
            
            val friend = Friend(
                id = friendId,
                nickname = nickname,
                userId = userId,
                status = Friend.FriendStatus.ACCEPTED // 模拟直接添加成功
            )
            
            database.friendDao().insertFriend(friend)
            Toast.makeText(this@StealthChatActivity, R.string.friend_added, Toast.LENGTH_SHORT).show()
            loadFriends()
        }
    }

    private fun showRemoveFriendDialog(friend: Friend) {
        AlertDialog.Builder(this)
            .setTitle(R.string.remove)
            .setMessage(R.string.confirm_remove_friend)
            .setPositiveButton(R.string.confirm) { _, _ ->
                removeFriend(friend)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun removeFriend(friend: Friend) {
        val userId = preferenceManager.getUserId() ?: return
        
        lifecycleScope.launch {
            database.friendDao().deleteFriendById(friend.id, userId)
            Toast.makeText(this@StealthChatActivity, R.string.friend_removed, Toast.LENGTH_SHORT).show()
            loadFriends()
        }
    }
}
