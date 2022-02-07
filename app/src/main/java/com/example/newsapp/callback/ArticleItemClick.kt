package com.example.newsapp.callback

import com.example.newsapp.model.Article

interface ArticleItemClick {
    fun onClick(article: Article)
}