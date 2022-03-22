package com.prox.powerpointreader.vm

import android.app.Application
import androidx.lifecycle.*
import com.prox.powerpointreader.db.PPTFileDao
import com.prox.powerpointreader.model.PPTFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PPTFileViewModel @Inject constructor(
    private val application: Application,
    private val pptFileDao: PPTFileDao
) : ViewModel() {


    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_NAME)
    private val pptFileFlow = combine(
        searchQuery,
        sortOrder
    ) { query, sortOrder ->
        Pair(query, sortOrder)
    }.flatMapLatest { (query, sortOrder) ->
        pptFileDao.getPptFile(query, sortOrder)
    }
    val pptFile = pptFileFlow.asLiveData()

    private val pptFileFlowLike = combine(
        searchQuery,
        sortOrder
    ) { query, sortOrder ->
        Pair(query, sortOrder)
    }.flatMapLatest { (query, sortOrder) ->
        pptFileDao.getPptFileLike(query, sortOrder)
    }
    val pptFileLike = pptFileFlowLike.asLiveData()


    fun addFile(pptFile: PPTFile) {
        viewModelScope.launch {
            pptFileDao.addFile(pptFile)
        }
    }

    fun deteleFile(pptFile: PPTFile) {
        viewModelScope.launch {
            pptFileDao.deleteFile(pptFile)
        }
    }

    fun deleteFileByPath(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            pptFileDao.deleteFileByAbsolutePath(path)
        }
    }

    fun updateFile(pptFile: PPTFile) {
        viewModelScope.launch(Dispatchers.Main) {
            pptFileDao.updateFile(pptFile)
        }
    }
}
enum class SortOrder { BY_NAME, BY_CREATE_DATE, BY_ACCESSED_DATE }