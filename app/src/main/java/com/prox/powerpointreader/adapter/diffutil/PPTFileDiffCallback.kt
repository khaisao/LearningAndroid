package com.prox.powerpointreader.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.prox.powerpointreader.model.PPTFile

class PPTFileDiffCallback:DiffUtil.ItemCallback<PPTFile>() {
    override fun areItemsTheSame(oldItem: PPTFile, newItem: PPTFile): Boolean {
        return oldItem.absolutePath == newItem.absolutePath
    }

    override fun areContentsTheSame(oldItem: PPTFile, newItem: PPTFile): Boolean {
        return  oldItem == newItem
    }
}