package com.example.newnews.domain

import com.example.newnews.data.factories.NewsApiFactory
import com.example.newnews.data.models.NewsCategory
import com.example.newnews.data.repo.NewsRepo

object NewsUseCase {
    val api = NewsApiFactory.newsApi

    suspend fun getNews(category: String): NewsCategory {
        return NewsRepo.getNews(category)
    }
}