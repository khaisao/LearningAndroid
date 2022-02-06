package com.example.music.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.music.MusicActivity
import com.example.music.fragment.AlbumFragment
import com.example.music.fragment.ArtistFragment
import com.example.music.fragment.SongFragment
import java.lang.IllegalArgumentException

class MusicPagerAdapter(private val musicActivity: MusicActivity):FragmentStateAdapter(musicActivity) {
    private val tabs = arrayOf("Song", "Album", "Artist")
    override fun getItemCount(): Int {
        return tabs.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SongFragment()
            1 -> AlbumFragment()
            2 -> ArtistFragment()
            else -> throw IllegalArgumentException("Unknow $position")
        }
    }
}