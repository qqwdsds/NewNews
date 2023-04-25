package com.example.newnews.data.models

data class NewsCategory(
    val category: String,
    val data: List<News>,
    val success: Boolean)
