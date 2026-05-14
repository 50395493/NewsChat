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

class TechNewsFragment : Fragment() {

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
            NewsItem("1", "iPhone 17 Pro 曝光：全新设计引领科技潮流", "苹果新一代旗舰手机将采用革命性的设计语言，配备更强大的摄像系统和处理器。", "科技", System.currentTimeMillis(), "https://example.com/1", "https://picsum.photos/400/200"),
            NewsItem("2", "AI 芯片技术突破：算力提升300%", "最新一代AI芯片在能效比方面取得重大突破，为智能设备带来更持久的续航体验。", "科技", System.currentTimeMillis() - 3600000, "https://example.com/2", "https://picsum.photos/400/201"),
            NewsItem("3", "华为发布鸿蒙5.0系统", "华为正式推出鸿蒙5.0操作系统，带来更流畅的用户体验和更强大的跨设备协同能力。", "科技", System.currentTimeMillis() - 7200000, "https://example.com/3", "https://picsum.photos/400/202"),
            NewsItem("4", "折叠屏手机价格降至3000元以内", "随着供应链成熟，折叠屏手机成本大幅下降，更多消费者可以体验这项新技术。", "科技", System.currentTimeMillis() - 10800000, "https://example.com/4", "https://picsum.photos/400/203"),
            NewsItem("5", "电动汽车充电速度革命：10分钟充满80%", "固态电池技术取得突破，电动汽车充电时间大幅缩短，彻底解决里程焦虑问题。", "科技", System.currentTimeMillis() - 14400000, "https://example.com/5", "https://picsum.photos/400/204")
        )
    }
}
