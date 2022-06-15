package com.example.newsapicompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapicompose.di.Resource
import com.example.newsapicompose.model.News
import com.example.newsapicompose.usecase.NewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    private val usecase : NewsUseCase
) : ViewModel(){

    private val _apiNews = MutableStateFlow<Resource<Response<News>>>(Resource.Idle())
    var apiNews: StateFlow<Resource<Response<News>>> = _apiNews


    fun getNews(country: String, key: String) {
        viewModelScope.launch {
            usecase.getNews(country = country,key = key).collect {result->
                when (result) {
                    is Resource.Loading -> {
                        _apiNews.value = Resource.Loading()
                        Timber.tag("Loading").e("")
                    }
                    is Resource.Success -> {
                        _apiNews.value = Resource.Success(result.data!!)
                        Timber.tag("Success").e("")
                    }
                    is Resource.Error -> {
                        _apiNews.value = Resource.Error("error")
                        Timber.tag("Error").e("")
                    }
                    is Resource.Idle -> {
                        _apiNews.value = Resource.Idle()
                    }
                }
            }
        }
    }
}