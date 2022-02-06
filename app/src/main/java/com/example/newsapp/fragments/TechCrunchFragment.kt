package com.example.newsapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.ArticleActivity
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapter.ArticleAdapter
import com.example.newsapp.callback.ArticleItemClick
import com.example.newsapp.databinding.FragmentTechCrunchBinding
import com.example.newsapp.model.Article
import com.example.newsapp.vm.ArticleViewModel


class TechCrunchFragment : Fragment(),ArticleItemClick {
    private lateinit var viewModel: ArticleViewModel
    private lateinit var binding:FragmentTechCrunchBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTechCrunchBinding.inflate(inflater)
        val adapter = ArticleAdapter(this)
        binding.rv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        binding.rv.adapter = adapter
        viewModel = (activity as MainActivity).viewModel
        viewModel.techcrunchList.observe(viewLifecycleOwner) {
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