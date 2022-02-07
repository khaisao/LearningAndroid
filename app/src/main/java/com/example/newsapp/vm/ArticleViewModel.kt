package com.example.newsapp.vm

import android.app.Application
import androidx.lifecycle.ViewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.api.ArticleAPI
import com.example.newsapp.db.ArticleDao
import com.example.newsapp.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(private val application: Application, private val articleDao:ArticleDao , private val articleAPI:ArticleAPI) : ViewModel() {

    var teslaList = MutableLiveData<List<Article>>()
    var searchList = MutableLiveData<List<Article>>()
    var techcrunchList = MutableLiveData<List<Article>>()
    var businessList = MutableLiveData<List<Article>>()
    val errorMessage = MutableLiveData<String>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllTesla()
            getAllTechCrunch()
            getAllBusiness()
        }
    }

    private suspend fun getAllTesla() {
        val response = articleAPI.getAllTesla().articles
        teslaList.postValue(response)
    }

    private suspend fun getAllTechCrunch() {
        val response = articleAPI.getAllTesla().articles
        techcrunchList.postValue(response)
    }

    private suspend fun getAllBusiness() {
        val response = articleAPI.getAllBusiness().articles
        businessList.postValue(response)
    }
     suspend fun searchArticle(searchQuery: String) {
        val response = articleAPI.searchForArticle(searchQuery)
        searchList.postValue(response.articles)
    }

    val articles = articleDao.getAllArticles()

    fun addArticle(article: Article) {
        viewModelScope.launch {
            articleDao.addArticle(article)
        }
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            articleDao.deleteArticle(article)
        }
    }


}