package com.prox.powerpointreader.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.prox.powerpointreader.model.PPTFile
import com.prox.powerpointreader.vm.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface PPTFileDao {

    fun getPptFile(query: String, sortOrder: SortOrder): Flow<List<PPTFile>> =
        when(sortOrder) {
            SortOrder.BY_NAME -> getAllFileSortedByName(query)
            SortOrder.BY_CREATE_DATE -> getAllFileSortedByCreateDate(query)
            SortOrder.BY_ACCESSED_DATE -> getAllFileSortedByAccessedDate(query)
        }
    @Query("SELECT * FROM pptfile WHERE fileName LIKE '%' || :search || '%' ORDER BY fileName DESC ")
    fun getAllFileSortedByName(search: String): Flow<List<PPTFile>>
    @Query("SELECT * FROM pptfile WHERE fileName LIKE '%' || :search || '%' ORDER BY createDate DESC ")
    fun getAllFileSortedByCreateDate(search: String): Flow<List<PPTFile>>
    @Query("SELECT * FROM pptfile WHERE fileName LIKE '%' || :search || '%' ORDER BY aceesedDate DESC ")
    fun getAllFileSortedByAccessedDate(search: String): Flow<List<PPTFile>>

    fun getPptFileLike(query: String, sortOrder: SortOrder): Flow<List<PPTFile>> =
        when(sortOrder) {
            SortOrder.BY_NAME -> getAllFileSortedByNameLike(query)
            SortOrder.BY_CREATE_DATE -> getAllFileSortedByCreateDateLike(query)
            SortOrder.BY_ACCESSED_DATE -> getAllFileSortedByAccessedDateLike(query)
        }
    @Query("SELECT * FROM pptfile WHERE fileName LIKE '%' || :search || '%' AND `like` = 1  ORDER BY fileName DESC ")
    fun getAllFileSortedByNameLike(search: String): Flow<List<PPTFile>>
    @Query("SELECT * FROM pptfile WHERE fileName LIKE '%' || :search || '%' AND `like` = 1 ORDER BY createDate DESC ")
    fun getAllFileSortedByCreateDateLike(search: String): Flow<List<PPTFile>>
    @Query("SELECT * FROM pptfile WHERE fileName LIKE '%' || :search || '%' AND `like` = 1 ORDER BY aceesedDate DESC ")
    fun getAllFileSortedByAccessedDateLike(search: String): Flow<List<PPTFile>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFile(pptFile: PPTFile)

    @Delete
    suspend fun deleteFile(pptFile: PPTFile)

    @Update
    suspend fun updateFile(pptFile: PPTFile)

    @Query("DELETE FROM pptfile WHERE absolutePath =:absolutePath")
    fun deleteFileByAbsolutePath(absolutePath: String?)




}