package com.example.newsapicompose.usecase

import com.example.newsapicompose.di.Resource
import com.example.newsapicompose.model.News
import com.example.newsapicompose.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class NewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    fun getNews(country: String, key: String): Flow<Resource<Response<News>>> {
        return flow {
            emit(Resource.Loading())
            val users = withContext(Dispatchers.IO) {
                repository.getNews(country = country, key = key)
            }
            emit(Resource.Success(users))
        }.catch() {
            Timber.tag("error").e("")
            emit(Resource.Error("An unexpected error occured"))
        }.catch() {
            Timber.tag("error").e("")
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}