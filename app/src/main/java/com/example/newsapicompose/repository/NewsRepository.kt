package com.example.newsapicompose.repository

import com.example.newsapicompose.model.News
import com.example.newsapicompose.service.NewsApi
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api : NewsApi
): NewsApi {
    override suspend fun getNews(country: String, key: String): News{
        return api.getNews(country = country,key = key)
    }
}