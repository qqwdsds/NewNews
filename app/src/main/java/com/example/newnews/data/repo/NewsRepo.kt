package com.example.newnews.data.repo

import com.example.newnews.data.models.NewsCategory
import com.example.newnews.domain.NewsUseCase

object NewsRepo {
    suspend fun getNews(category: String): NewsCategory {
        val news = NewsUseCase.api.getNews(category)
        return news
    }
}