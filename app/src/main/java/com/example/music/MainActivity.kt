package com.example.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.music.adapter.SongAdapter
import com.example.music.databinding.ActivityMainBinding
import com.example.music.fragment.AlbumFragment
import com.example.music.fragment.ArtistFragment
import com.example.music.fragment.SongFragment
import com.example.music.model.Song

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}