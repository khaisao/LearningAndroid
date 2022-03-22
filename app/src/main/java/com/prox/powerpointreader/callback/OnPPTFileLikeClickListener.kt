package com.prox.powerpointreader.callback

import com.prox.powerpointreader.model.PPTFile

interface OnPPTFileLikeClickListener {
    fun onClick(pptFile: PPTFile)
    fun onUnLikeClick(pptFile: PPTFile)
    fun onShareClick(pptFile: PPTFile)
}