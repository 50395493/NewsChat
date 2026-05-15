package com.stealthnews.chat.ui.chat

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stealthnews.chat.R
import com.stealthnews.chat.data.local.entity.ChatMessage
import com.stealthnews.chat.databinding.ItemChatMessageBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter(
    private val currentUserId: String
) : ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChatViewHolder(private val binding: ItemChatMessageBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(message: ChatMessage) {
            val isSent = message.senderId == currentUserId
            
            // 设置消息对齐
            val params = binding.messageContainer.layoutParams as LinearLayout.LayoutParams
            if (isSent) {
                params.gravity = Gravity.END
                binding.tvMessage.setBackgroundResource(R.drawable.bubble_sent)
                binding.tvMessage.setTextColor(binding.root.context.getColor(R.color.chat_text_sent))
            } else {
                params.gravity = Gravity.START
                binding.tvMessage.setBackgroundResource(R.drawable.bubble_received)
                binding.tvMessage.setTextColor(binding.root.context.getColor(R.color.chat_text_received))
            }
            binding.messageContainer.layoutParams = params
            
            binding.tvMessage.text = message.content
            binding.tvTime.text = SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            ).format(Date(message.timestamp))
        }
    }

    class ChatDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }
    }
}
