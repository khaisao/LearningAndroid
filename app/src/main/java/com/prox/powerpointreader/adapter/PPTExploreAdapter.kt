package com.prox.powerpointreader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.prox.powerpointreader.adapter.diffutil.PPTFileDiffCallback
import com.prox.powerpointreader.callback.OnPPTFileItemClickListener
import com.prox.powerpointreader.databinding.ItemExploreBinding
import com.prox.powerpointreader.model.PPTFile
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PPTExploreAdapter(private val callback: OnPPTFileItemClickListener) :
    ListAdapter<PPTFile, PPTExploreAdapter.ViewHolder>(PPTFileDiffCallback()) {
    private val viewBinderHelper = ViewBinderHelper()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemExploreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemExploreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pptFile: PPTFile) {
            viewBinderHelper.setOpenOnlyOne(true);
            viewBinderHelper.bind(binding.swLayout, pptFile.absolutePath)
            binding.tvFileName.text = pptFile.fileName
            binding.tvFileDate.text = pptFile.createDate
            if (pptFile.like == 0) {
                binding.ivUnlike.visibility = View.GONE
                binding.ivLike.visibility = View.VISIBLE
            } else {
                binding.ivLike.visibility = View.GONE
                binding.ivUnlike.visibility = View.VISIBLE
            }
            binding.clItem.setOnClickListener {
                callback.onClick(pptFile)
            }
            binding.ivLike.setOnClickListener {
                callback.onLikeClick(pptFile)
                binding.ivLike.visibility = View.GONE
                binding.ivUnlike.visibility = View.VISIBLE
                viewBinderHelper.closeLayout(pptFile.absolutePath)
            }
            binding.ivUnlike.setOnClickListener {
                callback.onUnLikeClick(pptFile)
                binding.ivUnlike.visibility = View.GONE
                binding.ivLike.visibility = View.VISIBLE
                viewBinderHelper.closeLayout(pptFile.absolutePath)
            }
            binding.ivEdit.setOnClickListener {
                callback.onRenameClick(pptFile)
                viewBinderHelper.closeLayout(pptFile.absolutePath)
            }
            binding.ivDelete.setOnClickListener {
                callback.onDeleteClick(pptFile)
                viewBinderHelper.closeLayout(pptFile.absolutePath)
            }
            binding.ivShare.setOnClickListener {
                callback.onShareClick(pptFile)
                viewBinderHelper.closeLayout(pptFile.absolutePath)
            }
        }
    }
}