package com.example.music.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.music.adapter.SongAdapter
import com.example.music.databinding.FragmentSongBinding
import com.example.music.model.Song

class SongFragment:Fragment() {
    private lateinit var binding:FragmentSongBinding
    private val my_song = arrayOf(
        Song("Nắng ấm xa dần", "Sơn Tùng"),
        Song("Nắng ấm xa dần", "Sơn Tùng"),
        Song("Nắng ấm xa dần", "Sơn Tùng"),
        Song("Nắng ấm xa dần", "Sơn Tùng"),
        Song("Nắng ấm xa dần", "Sơn Tùng"),
        Song("Nắng ấm xa dần", "Sơn Tùng"),
        Song("Nắng ấm xa dần", "Sơn Tùng")

    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongBinding.inflate(inflater)
        val adapter = SongAdapter(my_song)
        binding.rvSong.adapter = adapter
        binding.rvSong.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        return binding.root
    }
}