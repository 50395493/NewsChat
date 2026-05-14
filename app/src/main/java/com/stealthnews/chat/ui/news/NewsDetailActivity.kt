package com.stealthnews.chat.ui.news

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stealthnews.chat.data.model.NewsItem
import com.stealthnews.chat.databinding.ActivityNewsDetailBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsDetailBinding

    companion object {
        const val EXTRA_NEWS_ID = "news_id"
        const val EXTRA_NEWS_TITLE = "news_title"
        const val EXTRA_NEWS_CONTENT = "news_content"
        const val EXTRA_NEWS_SOURCE = "news_source"
        const val EXTRA_NEWS_TIME = "news_time"
        const val EXTRA_NEWS_URL = "news_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadNewsDetail()
        setupShareButton()
        setupSwipeRefresh()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun loadNewsDetail() {
        val title = intent.getStringExtra(EXTRA_NEWS_TITLE) ?: ""
        val content = intent.getStringExtra(EXTRA_NEWS_CONTENT) ?: ""
        val source = intent.getStringExtra(EXTRA_NEWS_SOURCE) ?: "未知来源"
        val time = intent.getLongExtra(EXTRA_NEWS_TIME, System.currentTimeMillis())
        val url = intent.getStringExtra(EXTRA_NEWS_URL)

        binding.tvTitle.text = title
        binding.tvContent.text = generateFullContent(content)
        binding.tvSource.text = source
        binding.tvTime.text = formatTime(time)

        // 保存URL用于分享
        binding.btnShare.tag = url
    }

    private fun generateFullContent(summary: String): String {
        // 生成更丰富的新闻内容（实际项目中应从API获取完整内容）
        return """
            |$summary
            |
            |【相关阅读】
            |
            |$summary
            |
            |【编辑点评】
            |
            |这篇报道引发了广泛讨论。业内人士表示，这一趋势将对相关行业产生深远影响。我们将持续关注后续发展。
            |
            |【相关背景】
            |
            |据悉，该领域近年来发展迅速，技术创新不断涌现。专家预测，未来的市场潜力巨大。
            |
            |更多详细信息，请关注我们的后续报道。
        """.trimMargin()
    }

    private fun formatTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60 * 1000 -> "刚刚"
            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前"
            diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}小时前"
            diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)}天前"
            else -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                sdf.format(Date(timestamp))
            }
        }
    }

    private fun setupShareButton() {
        binding.btnShare.setOnClickListener {
            val shareText = buildString {
                append(binding.tvTitle.text)
                append("\n\n")
                append("来源：${binding.tvSource.text}")
                append("\n")
                append(binding.btnShare.tag?.toString() ?: "")
                append("\n\n")
                append("— 来自${getString(com.stealthnews.chat.R.string.app_name)}")
            }

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(intent, "分享新闻"))
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            Toast.makeText(this, "已是最新内容", Toast.LENGTH_SHORT).show()
            binding.swipeRefresh.isRefreshing = false
        }
    }
}
