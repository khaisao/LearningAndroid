package com.prox.powerpointreader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.prox.powerpointreader.adapter.diffutil.PPTFileDiffCallback
import com.prox.powerpointreader.callback.OnPPTFileItemClickListener
import com.prox.powerpointreader.callback.OnPPTFileLikeClickListener
import com.prox.powerpointreader.databinding.ItemBookmarkBinding
import com.prox.powerpointreader.databinding.ItemExploreBinding
import com.prox.powerpointreader.model.PPTFile
import java.text.DateFormat

class PPTBookmarkAdapter(private val callback: OnPPTFileLikeClickListener) :
    ListAdapter<PPTFile, PPTBookmarkAdapter.ViewHolder>(PPTFileDiffCallback()) {
    private val viewBinderHelper = ViewBinderHelper()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookmarkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pptFile: PPTFile) {
            viewBinderHelper.setOpenOnlyOne(true);
            viewBinderHelper.bind(binding.swLayout, pptFile.absolutePath)
            binding.tvFileName.text = pptFile.fileName
            binding.tvFileDate.text = pptFile.createDate
            if (pptFile.like == 0) {
                binding.ivLike.visibility = View.VISIBLE
                binding.ivUnlike.visibility = View.INVISIBLE
            } else {
                binding.ivLike.visibility = View.INVISIBLE
                binding.ivUnlike.visibility = View.VISIBLE
            }
            binding.clItem.setOnClickListener {
                callback.onClick(pptFile)
            }
            binding.ivUnlike.setOnClickListener {
                callback.onUnLikeClick(pptFile)
                binding.ivUnlike.visibility = View.GONE
                binding.ivLike.visibility = View.VISIBLE
                viewBinderHelper.closeLayout(pptFile.absolutePath)

            }
            binding.ivShare.setOnClickListener {
                callback.onShareClick(pptFile)
                viewBinderHelper.closeLayout(pptFile.absolutePath)

            }
        }
    }
}