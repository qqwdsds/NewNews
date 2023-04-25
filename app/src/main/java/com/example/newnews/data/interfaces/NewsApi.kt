package com.example.newnews.data.interfaces

import com.example.newnews.data.models.NewsCategory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApi {
    @GET("news")
    suspend fun getNews(@Query("category") category: String): NewsCategory
}