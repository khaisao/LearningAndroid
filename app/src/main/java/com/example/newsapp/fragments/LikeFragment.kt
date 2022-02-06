package com.example.newsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.ArticleActivity
import com.example.newsapp.MainActivity
import com.example.newsapp.adapter.SearchArticleAdapter
import com.example.newsapp.callback.ArticleItemClick
import com.example.newsapp.databinding.FragmentLikeBinding
import com.example.newsapp.model.Article
import com.example.newsapp.vm.ArticleViewModel

class LikeFragment: Fragment(),ArticleItemClick {
    private lateinit var viewModel: ArticleViewModel
    private lateinit var binding:FragmentLikeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikeBinding.inflate(inflater)
        val adapter = SearchArticleAdapter(this)
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        viewModel = (activity as MainActivity).viewModel
        viewModel.articles.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        return binding.root
    }

    override fun onClick(article: Article) {
        val intent = Intent(context,ArticleActivity::class.java)
        intent.putExtra("article",article)
        startActivity(intent)
    }
}