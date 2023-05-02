package com.example.newnews.presentation.fragments.newslistfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnews.data.local.Categories
import com.example.newnews.data.models.News
import com.example.newnews.domain.NewsUseCase
import kotlinx.coroutines.launch

class NewsListViewModel: ViewModel() {

    private val _newsLiveData= MutableLiveData<List<News>>()
    val newsLiveData: LiveData<List<News>> = _newsLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _categoryLiveDate = MutableLiveData<List<String>>()
    val categoryLiveData: LiveData<List<String>> = _categoryLiveDate

    init{
        getNewsData("technology")
        _categoryLiveDate.postValue(Categories.getCategories())
    }

    fun getNewsData(category: String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            val news = NewsUseCase.getNews(category)
            _newsLiveData.postValue(news.data)
            _isLoading.postValue(false)
        }
    } // end getNewsData
}