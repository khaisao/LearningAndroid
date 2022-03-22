package com.prox.powerpointreader.di

import android.app.Application
import androidx.room.Room
import com.prox.powerpointreader.db.PPTFileDao
import com.prox.powerpointreader.db.PPTFileDatabase
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
    fun provideArticleDatabase(app: Application): PPTFileDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            PPTFileDatabase::class.java,
            "pptfile.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(db: PPTFileDatabase): PPTFileDao {
        return db.pptFileDao()
    }
}