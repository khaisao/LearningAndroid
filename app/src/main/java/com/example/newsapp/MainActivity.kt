package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.vm.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    val viewModel: ArticleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.btNavigation.setupWithNavController(navController)
        binding.ivSearch.setOnClickListener {
            val intent= Intent(this,SearchActivity::class.java)
            startActivity(intent)
        }
    }
}