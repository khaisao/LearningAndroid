package com.example.newsapp.vm

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.model.Article
import com.example.newsapp.model.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class ArticleViewModel(application: Application) :
    ViewModel() {

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
        val response = RetrofitInstance.invoke().getAllTesla().articles
        teslaList.postValue(response)
    }

    private suspend fun getAllTechCrunch() {
        val response = RetrofitInstance.invoke().getAllTechCrunch().articles
        techcrunchList.postValue(response)
    }

    private suspend fun getAllBusiness() {
        val response = RetrofitInstance.invoke().getAllBusiness().articles
        businessList.postValue(response)
    }
     suspend fun searchArticle(searchQuery: String) {
        val response = RetrofitInstance.invoke().searchForArticle(searchQuery)
        searchList.postValue(response.articles)
    }

    private val articleDao = ArticleDatabase.invoke(application).articleDao()
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