package com.stealthnews.chat.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stealthnews.chat.data.model.Friend
import com.stealthnews.chat.databinding.ItemFriendBinding

class FriendAdapter(
    private val onAddClick: (Friend) -> Unit,
    private val onRemoveClick: (Friend) -> Unit,
    private val onItemClick: ((Friend) -> Unit)? = null,
    private val showRemoveButton: Boolean = false
) : ListAdapter<Friend, FriendAdapter.FriendViewHolder>(FriendDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ItemFriendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FriendViewHolder(
        private val binding: ItemFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(getItem(position))
                }
            }
            
            binding.btnAdd.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onAddClick(getItem(position))
                }
            }
            
            binding.btnRemove.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onRemoveClick(getItem(position))
                }
            }
        }

        fun bind(friend: Friend) {
            binding.tvName.text = friend.note ?: friend.nickname
            binding.tvId.text = "ID: ${friend.id}"
            
            if (showRemoveButton) {
                binding.btnAdd.visibility = View.GONE
                binding.btnRemove.visibility = View.VISIBLE
            } else {
                binding.btnAdd.visibility = View.VISIBLE
                binding.btnRemove.visibility = View.GONE
            }
        }
    }

    class FriendDiffCallback : DiffUtil.ItemCallback<Friend>() {
        override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            return oldItem == newItem
        }
    }
}
