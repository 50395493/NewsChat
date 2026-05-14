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
            NewsItem("6", "中超联赛：国安逆转战胜恒大", "北京国安在主场0-2落后的情况下连扳3球，完成惊天大逆转，送给恒大赛季首败。", "https://picsum.photos/400/205", "体育频道", System.currentTimeMillis(), "体育"),
            NewsItem("7", "NBA季后赛：湖人横扫太阳晋级", "詹姆斯砍下三双数据，带领湖人4-0横扫太阳，强势晋级西部决赛。", "https://picsum.photos/400/206", "NBA中国", System.currentTimeMillis() - 3600000, "体育"),
            NewsItem("8", "中国女排3-0横扫日本夺冠", "世界女排联赛总决赛，中国队以3-0完胜日本队，成功夺得冠军。", "https://picsum.photos/400/207", "排球世界", System.currentTimeMillis() - 7200000, "体育"),
            NewsItem("9", "欧冠决赛：皇马第16次捧杯", "皇家马德里1-0战胜曼城，历史上第16次夺得欧冠冠军，继续领跑这项赛事。", "https://picsum.photos/400/208", "足球迷", System.currentTimeMillis() - 10800000, "体育"),
            NewsItem("10", "F1上海站：维斯塔潘夺冠", "红牛车手维斯塔潘在上海国际赛车场夺得新赛季第三场胜利。", "https://picsum.photos/400/209", "赛车频道", System.currentTimeMillis() - 14400000, "体育")
        )
    }
}
