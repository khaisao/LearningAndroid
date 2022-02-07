package com.example.newsapp.di

import android.app.Application
import androidx.room.Room
import com.example.newsapp.db.ArticleDao
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.model.Article
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideArticleDatabase(app: Application): ArticleDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            ArticleDatabase::class.java,
            "article.db"
        ).build()
    }
    @Provides
    @Singleton
    fun provideArticleDao(db:ArticleDatabase):ArticleDao{
        return db.articleDao()
    }
}