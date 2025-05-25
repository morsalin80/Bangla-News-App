package com.example.banglanews.model

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("news")
    suspend fun getNewsByCategory(
        @Query("apikey") apiKey: String,
        @Query("country") country: String = "bd",
        @Query("language") language: String = "bn",
        @Query("category") category: String
    ): NewsResponse
}