package com.example.banglanews.model

class NewsRepository(private val apiService: NewsApiService) {
    suspend fun getNews(category: String): List<NewsArticle> {
        return try {
            apiService.getNewsByCategory(
                apiKey = "pub_888521d895614f678c47897b28d57abd",
                category = category
            ).results ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

