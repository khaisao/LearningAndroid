package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsapp.databinding.ActivitySplashBinding
import kotlinx.coroutines.*


class SplashActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySplashBinding
    val activityScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activityScope.launch {
            delay(3000)
            var intent = Intent(this@SplashActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}