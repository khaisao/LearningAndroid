package com.prox.powerpointreader.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.prox.powerpointreader.model.PPTFile

@Database(
    entities = [PPTFile::class],
    exportSchema = false,
    version = 2
)
abstract class PPTFileDatabase:RoomDatabase() {
    abstract  fun pptFileDao():PPTFileDao
}