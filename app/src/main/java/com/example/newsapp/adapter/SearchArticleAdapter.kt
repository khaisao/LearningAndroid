package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemArticleSearchBinding
import com.example.newsapp.adapter.diffutil.ArticleDiffCallback
import com.example.newsapp.callback.ArticleItemClick

import com.example.newsapp.model.Article

class SearchArticleAdapter(private val callback: ArticleItemClick) :
    ListAdapter<Article, SearchArticleAdapter.ViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemArticleSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemArticleSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            Glide.with(binding.ivArticle)
                .load(article.urlToImage)
                .into(binding.ivArticle)
            binding.tvTitle.text = article.title
            binding.tvPubDate.text = article.publishedAt
            binding.itemArticle.setOnClickListener {
                callback.onClick(article)
            }
        }
    }
}