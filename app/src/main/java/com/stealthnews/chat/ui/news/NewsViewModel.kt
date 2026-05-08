package com.stealthnews.chat.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stealthnews.chat.data.model.NewsItem
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val _news = MutableLiveData<List<NewsItem>>()
    val news: LiveData<List<NewsItem>> = _news

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadNews() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // TODO: 从API加载新闻
                _news.value = emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
