package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.adapter.diffutil.ArticleDiffCallback
import com.example.newsapp.callback.ArticleItemClick
import com.example.newsapp.databinding.ItemArticleBinding
import com.example.newsapp.model.Article


class ArticleAdapter(private val callback:ArticleItemClick):ListAdapter<Article,ArticleAdapter.ViewHolder>(
    ArticleDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    inner class ViewHolder(private var binding:ItemArticleBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(article:Article){
            Glide.with(binding.ivNews)
                .load(article.urlToImage)
                .into(binding.ivNews)
            binding.tvTitle.text = article.title
            binding.tvDes.text = article.description
            binding.tvPub.text = article.publishedAt
            binding.itemArticle.setOnClickListener {
                callback.onClick(article)
            }
        }
    }
}