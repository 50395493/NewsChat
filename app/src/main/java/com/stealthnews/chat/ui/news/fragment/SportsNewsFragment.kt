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

class SportsNewsFragment : Fragment() {

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
            NewsItem("6", "中超联赛：国安逆转战胜恒大", "北京国安在主场0-2落后的情况下连扳3球，完成惊天大逆转，送给恒大赛季首败。", "体育", System.currentTimeMillis(), "https://example.com/6", "https://picsum.photos/400/205"),
            NewsItem("7", "NBA季后赛：湖人横扫太阳晋级", "詹姆斯砍下三双数据，带领湖人4-0横扫太阳，强势晋级西部决赛。", "体育", System.currentTimeMillis() - 3600000, "https://example.com/7", "https://picsum.photos/400/206"),
            NewsItem("8", "中国女排3-0横扫日本夺冠", "世界女排联赛总决赛，中国队以3-0完胜日本队，成功夺得冠军。", "体育", System.currentTimeMillis() - 7200000, "https://example.com/8", "https://picsum.photos/400/207"),
            NewsItem("9", "欧冠决赛：皇马第16次捧杯", "皇家马德里1-0战胜曼城，历史上第16次夺得欧冠冠军，继续领跑这项赛事。", "体育", System.currentTimeMillis() - 10800000, "https://example.com/9", "https://picsum.photos/400/208"),
            NewsItem("10", "F1上海站：维斯塔潘夺冠", "红牛车手维斯塔潘在上海国际赛车场夺得新赛季第三场胜利。", "体育", System.currentTimeMillis() - 14400000, "https://example.com/10", "https://picsum.photos/400/209")
        )
    }
}
