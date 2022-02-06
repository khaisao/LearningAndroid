package com.example.music.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.music.adapter.AlbumAdapter
import com.example.music.adapter.SongAdapter
import com.example.music.databinding.FragmentAlbumBinding
import com.example.music.model.Song

class AlbumFragment:Fragment() {
    private lateinit var binding:FragmentAlbumBinding
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
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater)
        binding.rvAlbum.adapter = AlbumAdapter(my_song)
        binding.rvAlbum.layoutManager = GridLayoutManager(requireContext(),3)
        return binding.root
    }
}