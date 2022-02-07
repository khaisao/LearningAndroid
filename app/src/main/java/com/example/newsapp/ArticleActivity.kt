package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels

import com.example.newsapp.databinding.ActivityArticleBinding
import com.example.newsapp.model.Article
import com.example.newsapp.vm.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class ArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    val viewModel: ArticleViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityArticleBinding.inflate(layoutInflater)
        val article=intent.getSerializableExtra("article") as Article
        binding.webview.loadUrl(article.url)
        setContentView(binding.root)
        binding.ivSaveArticle.setOnClickListener {
            viewModel.addArticle(article)
            Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
        }
    }
}