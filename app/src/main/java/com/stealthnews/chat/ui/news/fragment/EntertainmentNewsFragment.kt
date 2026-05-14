package com.stealthnews.chat.ui.news.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stealthnews.chat.R
import com.stealthnews.chat.data.model.NewsItem

class EntertainmentNewsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = NewsListAdapter(getMockNews())
        recyclerView.adapter = adapter
    }

    private fun getMockNews(): List<NewsItem> {
        return listOf(
            NewsItem("11", "《流浪地球3》定档春节，刘慈欣担任编剧", "备受期待的科幻巨制《流浪地球3》正式宣布定档2026年春节，阵容全面升级。", "娱乐", System.currentTimeMillis(), "https://example.com/11", "https://picsum.photos/400/210"),
            NewsItem("12", "周杰伦新专辑《最伟大的作品2》发布", "时隔三年，周杰伦携新专辑回归，首支单曲24小时播放量破亿。", "娱乐", System.currentTimeMillis() - 3600000, "https://example.com/12", "https://picsum.photos/400/211"),
            NewsItem("13", "戛纳电影节：中国影片《繁花》获金棕榈", "王家卫执导的《繁花》在第78届戛纳电影节获得最佳影片大奖。", "娱乐", System.currentTimeMillis() - 7200000, "https://example.com/13", "https://picsum.photos/400/212"),
            NewsItem("14", "Netflix热门剧集《鱿鱼游戏2》续订", "现象级剧集《鱿鱼游戏》第二季正式续订，将于明年与观众见面。", "娱乐", System.currentTimeMillis() - 10800000, "https://example.com/14", "https://picsum.photos/400/213"),
            NewsItem("15", "TFBOYS十周年演唱会官宣", "TFBOYS正式官宣十周年演唱会，三小只将再次同台，引发粉丝热议。", "娱乐", System.currentTimeMillis() - 14400000, "https://example.com/15", "https://picsum.photos/400/214")
        )
    }
}
