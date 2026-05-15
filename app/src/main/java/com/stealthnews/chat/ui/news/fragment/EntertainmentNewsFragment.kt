package com.stealthnews.chat.ui.news.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stealthnews.chat.R
import com.stealthnews.chat.ui.news.NewsViewModel

class EntertainmentNewsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsListAdapter
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(requireActivity())[NewsViewModel::class.java]
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = NewsListAdapter(emptyList())
        recyclerView.adapter = adapter
        
        // 观察ViewModel的新闻数据
        viewModel.news.observe(viewLifecycleOwner) { newsList ->
            val entertainmentNews = newsList.filter { it.category == "娱乐" }
            adapter.updateData(entertainmentNews)
        }
    }
}
