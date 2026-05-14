package com.stealthnews.chat.ui.news.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stealthnews.chat.data.model.NewsItem
import com.stealthnews.chat.databinding.ItemNewsBinding
import com.stealthnews.chat.ui.news.NewsDetailActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class NewsListAdapter(
    private var newsList: List<NewsItem>
) : RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // 设置点击事件
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val news = newsList[position]
                    navigateToDetail(news)
                }
            }
        }

        fun bind(news: NewsItem) {
            binding.tvTitle.text = news.title
            binding.tvSummary.text = news.description
            binding.tvCategory.text = news.category
            binding.tvTime.text = getTimeAgo(news.publishedAt)
        }

        private fun navigateToDetail(news: NewsItem) {
            val context = binding.root.context
            val intent = android.content.Intent(context, NewsDetailActivity::class.java).apply {
                putExtra(NewsDetailActivity.EXTRA_NEWS_ID, news.id)
                putExtra(NewsDetailActivity.EXTRA_NEWS_TITLE, news.title)
                putExtra(NewsDetailActivity.EXTRA_NEWS_CONTENT, news.description)
                putExtra(NewsDetailActivity.EXTRA_NEWS_SOURCE, news.source)
                putExtra(NewsDetailActivity.EXTRA_NEWS_TIME, news.publishedAt)
                putExtra(NewsDetailActivity.EXTRA_NEWS_URL, news.url ?: "")
            }
            context.startActivity(intent)
        }

        private fun getTimeAgo(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp

            return when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "刚刚"
                diff < TimeUnit.HOURS.toMillis(1) -> "${diff / TimeUnit.MINUTES.toMillis(1)}分钟前"
                diff < TimeUnit.DAYS.toMillis(1) -> "${diff / TimeUnit.HOURS.toMillis(1)}小时前"
                diff < TimeUnit.DAYS.toMillis(7) -> "${diff / TimeUnit.DAYS.toMillis(1)}天前"
                else -> {
                    val sdf = SimpleDateFormat("MM-dd", Locale.getDefault())
                    sdf.format(Date(timestamp))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

    fun updateData(newList: List<NewsItem>) {
        newsList = newList
        notifyDataSetChanged()
    }
}
