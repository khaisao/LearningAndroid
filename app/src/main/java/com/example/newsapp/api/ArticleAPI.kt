package com.example.newsapp.api

import com.example.newsapp.model.NewsResponse
import com.example.newsapp.util.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ArticleAPI {
    @GET("v2/everything")
    suspend fun getAllTesla(
        @Query("q")
        searchQuery: String="tesla",
        @Query("apiKey")
        apiKey: String = Constants.API_KEY
    ): NewsResponse

    @GET("v2/top-headlines?country=us&category=business&apiKey=15ef12bf28eb492b94ca2c2719c0ee47")
    suspend fun getAllBusiness(): NewsResponse

    @GET("v2/top-headlines?sources=techcrunch&apiKey=15ef12bf28eb492b94ca2c2719c0ee47")
    suspend fun getAllTechCrunch(): NewsResponse

    @GET("v2/everything")
    suspend fun searchForArticle(
        @Query("q")
        searchQuery: String,
        @Query("apiKey")
        apiKey: String = Constants.API_KEY
    ): NewsResponse

}