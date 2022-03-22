package com.prox.powerpointreader.callback

import com.prox.powerpointreader.model.PPTFile

interface OnPPTFileItemClickListener {
    fun onClick(pptFile: PPTFile)
    fun onLikeClick(pptFile: PPTFile)
    fun onUnLikeClick(pptFile: PPTFile)
    fun onShareClick(pptFile: PPTFile)
    fun onRenameClick(pptFile: PPTFile)
    fun onDeleteClick(pptFile: PPTFile)
}