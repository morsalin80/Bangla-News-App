package com.example.banglanews.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banglanews.model.NewsArticle
import com.example.banglanews.model.NewsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _articles = MutableStateFlow<List<NewsArticle>>(emptyList())
    val articles: StateFlow<List<NewsArticle>> = _articles.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentFetchJob: Job? = null

    fun fetchNews(category: String) {
        // Cancel any previous fetch request to prevent race conditions
        currentFetchJob?.cancel()

        currentFetchJob = viewModelScope.launch {
            // Clear old data immediately to prevent showing stale data
            _articles.value = emptyList()
            _isLoading.value = true

            try {
                _articles.value = repository.getNews(category)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
