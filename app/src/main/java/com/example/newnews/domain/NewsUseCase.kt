package com.example.newnews.domain

import com.example.newnews.data.factories.NewsApiFactory
import com.example.newnews.data.models.NewsCategory

object NewsUseCase {
    val api = NewsApiFactory.newsApi

    suspend fun getNews(category: String): NewsCategory {
        val news = api.getNews(category)
        return news
    }
}