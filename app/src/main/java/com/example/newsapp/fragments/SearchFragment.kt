package com.example.newsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.ArticleActivity
import com.example.newsapp.MainActivity
import com.example.newsapp.adapter.ArticleAdapter
import com.example.newsapp.callback.ArticleItemClick
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.model.Article
import com.example.newsapp.vm.ArticleViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment:Fragment(),ArticleItemClick {
    private lateinit var viewModel: ArticleViewModel
    private lateinit var binding:FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        val adapter = ArticleAdapter(this)
        binding.rv.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        binding.rv.adapter = adapter
        viewModel = (activity as MainActivity).viewModel
        var job: Job? = null
        binding.edtSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                       viewModel.searchArticle(editable.toString())
                        Log.d("khai",editable.toString())
                    }
                }
            }
        }
        viewModel.searchList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        return binding.root
    }

    override fun onClick(article: Article) {
        val intent = Intent(context, ArticleActivity::class.java)
        intent.putExtra("article",article)
        startActivity(intent)
    }
}