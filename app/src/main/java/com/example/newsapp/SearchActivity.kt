package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapter.SearchArticleAdapter
import com.example.newsapp.callback.ArticleItemClick
import com.example.newsapp.databinding.ActivitySearchBinding
import com.example.newsapp.model.Article
import com.example.newsapp.vm.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(),ArticleItemClick {
    private lateinit var binding:ActivitySearchBinding
    private val viewModel:ArticleViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter =SearchArticleAdapter(this)
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.ivBack.setOnClickListener {
            finish()
        }
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
        viewModel.searchList.observe(this){
            adapter.submitList(it)
        }
    }

    override fun onClick(article: Article) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra("article",article)
        startActivity(intent)
    }
}