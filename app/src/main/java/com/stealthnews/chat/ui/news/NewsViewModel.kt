package com.stealthnews.chat.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stealthnews.chat.data.model.NewsItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val _news = MutableLiveData<List<NewsItem>>()
    val news: LiveData<List<NewsItem>> = _news

    private val _newsList = MutableLiveData<List<NewsItem>>()
    val newsList: LiveData<List<NewsItem>> = _newsList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchResults = MutableLiveData<List<NewsItem>>()
    val searchResults: LiveData<List<NewsItem>> = _searchResults

    // 存储所有新闻用于搜索
    private var allNews: List<NewsItem> = emptyList()

    init {
        // 初始化时加载新闻
        loadNews()
    }

    fun loadNews() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // 模拟网络延迟
                delay(500)
                
                // 加载模拟新闻数据
                allNews = getMockNews()
                _news.value = allNews
                _newsList.value = allNews
            } catch (e: Exception) {
                e.printStackTrace()
                _news.value = emptyList()
                _newsList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchNews(query: String) {
        if (query.isEmpty()) {
            _searchResults.value = allNews
            return
        }
        
        _isLoading.value = true
        viewModelScope.launch {
            try {
                delay(300)
                val results = allNews.filter { news ->
                    news.title.contains(query, ignoreCase = true) ||
                    news.description.contains(query, ignoreCase = true) ||
                    news.category.contains(query, ignoreCase = true)
                }
                _searchResults.value = results
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshNews() {
        loadNews()
    }

    private fun getMockNews(): List<NewsItem> {
        val now = System.currentTimeMillis()
        return listOf(
            // 科技新闻
            NewsItem("1", "iPhone 17 Pro 曝光：全新设计引领科技潮流", 
                "苹果新一代旗舰手机将采用革命性的设计语言，配备更强大的摄像系统和全新处理器架构据悉，苹果公司正在秘密研发新一代iPhone旗舰机型。", 
                "https://picsum.photos/800/400?random=1", "科技资讯", now, "科技"),
            NewsItem("2", "AI 芯片技术突破：算力提升300%", 
                "最新一代AI芯片在能效比方面取得重大突破，为智能设备带来更持久的续航体验和更强的本地AI能力。", 
                "https://picsum.photos/800/400?random=2", "科技前沿", now - 3600000, "科技"),
            NewsItem("3", "华为发布鸿蒙5.0系统", 
                "华为正式推出鸿蒙5.0操作系统，带来更流畅的用户体验和更强大的跨设备协同能力，支持更多设备互联。", 
                "https://picsum.photos/800/400?random=3", "华为官方", now - 7200000, "科技"),
            NewsItem("4", "折叠屏手机价格降至3000元以内", 
                "随着供应链成熟和制造成本下降，折叠屏手机价格大幅下降，更多消费者可以体验这项新技术。", 
                "https://picsum.photos/800/400?random=4", "数码之家", now - 10800000, "科技"),
            NewsItem("5", "电动汽车充电速度革命：10分钟充满80%", 
                "固态电池技术取得突破，电动汽车充电时间大幅缩短，彻底解决用户的里程焦虑问题。", 
                "https://picsum.photos/800/400?random=5", "新能源观察", now - 14400000, "科技"),
            
            // 体育新闻
            NewsItem("6", "中超联赛：国安逆转战胜恒大", 
                "北京国安在主场0-2落后的情况下连扳3球，完成惊天大逆转，送给恒大赛季首败，球迷沸腾。", 
                "https://picsum.photos/800/400?random=6", "体育频道", now, "体育"),
            NewsItem("7", "NBA季后赛：湖人横扫太阳晋级", 
                "詹姆斯砍下三双数据，带领湖人4-0横扫太阳，强势晋级西部决赛，剑指总冠军。", 
                "https://picsum.photos/800/400?random=7", "NBA中国", now - 3600000, "体育"),
            NewsItem("8", "中国女排3-0横扫日本夺冠", 
                "世界女排联赛总决赛，中国队以3-0完胜日本队，成功夺得冠军，为国争光。", 
                "https://picsum.photos/800/400?random=8", "排球世界", now - 7200000, "体育"),
            NewsItem("9", "欧冠决赛：皇马第16次捧杯", 
                "皇家马德里1-0战胜曼城，历史上第16次夺得欧冠冠军，继续领跑这项赛事。", 
                "https://picsum.photos/800/400?random=9", "足球迷", now - 10800000, "体育"),
            NewsItem("10", "F1上海站：维斯塔潘夺冠", 
                "红牛车手维斯塔潘在上海国际赛车场夺得新赛季第三场胜利，领先优势继续扩大。", 
                "https://picsum.photos/800/400?random=10", "赛车频道", now - 14400000, "体育"),
            
            // 娱乐新闻
            NewsItem("11", "《流浪地球3》定档春节，刘慈欣担任编剧", 
                "备受期待的科幻巨制《流浪地球3》正式宣布定档2026年春节，阵容全面升级，特效更震撼。", 
                "https://picsum.photos/800/400?random=11", "影视资讯", now, "娱乐"),
            NewsItem("12", "周杰伦新专辑《最伟大的作品2》发布", 
                "时隔三年，周杰伦携新专辑回归，首支单曲24小时播放量破亿，再创华语乐坛纪录。", 
                "https://picsum.photos/800/400?random=12", "音乐之声", now - 3600000, "娱乐"),
            NewsItem("13", "戛纳电影节：中国影片《繁花》获金棕榈", 
                "王家卫执导的《繁花》在第78届戛纳电影节获得最佳影片大奖，华语电影再创辉煌。", 
                "https://picsum.photos/800/400?random=13", "国际电影节", now - 7200000, "娱乐"),
            NewsItem("14", "Netflix热门剧集《鱿鱼游戏2》续订", 
                "现象级剧集《鱿鱼游戏》第二季正式续订，将于明年与观众见面，全球粉丝期待。", 
                "https://picsum.photos/800/400?random=14", "流媒体观察", now - 10800000, "娱乐"),
            NewsItem("15", "TFBOYS十周年演唱会官宣", 
                "TFBOYS正式官宣十周年演唱会，三小只将再次同台，引发粉丝热议，门票秒空。", 
                "https://picsum.photos/800/400?random=15", "娱乐周刊", now - 14400000, "娱乐")
        )
    }
}
