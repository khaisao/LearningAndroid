package com.example.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import com.example.music.adapter.MusicPagerAdapter
import com.example.music.databinding.ActivityMusicBinding
import com.example.music.model.Song
import com.google.android.material.tabs.TabLayoutMediator

class MusicActivity : AppCompatActivity() {
    private val tabs = arrayOf("Song", "Album", "Artist")
    val my_song = arrayOf(
        Song("Nắng ấm xa dần", "Sơn Tùng"),
        Song("Nắng ấm xa dần", "Sơn Tùng"),
        Song("Nắng ấm xa dần", "Sơn Tùng"),
        Song("Nắng ấm xa dần", "Sơn Tùng"),
        Song("Nắng ấm xa dần", "Sơn Tùng"),
        Song("Nắng ấm xa dần", "Sơn Tùng"),
        Song("Nắng ấm xa dần", "Sơn Tùng")
    )
    private lateinit var binding:ActivityMusicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMusicBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val adapter = MusicPagerAdapter(this)
        binding.vpMusic.adapter = adapter
        TabLayoutMediator(binding.tlMain, binding.vpMusic){
                tab, index -> tab.text = tabs[index]
        }.attach()
        binding.imgMenu.setOnClickListener {
            if(binding.dlMain.isDrawerOpen(GravityCompat.START)){
                binding.dlMain.closeDrawer(GravityCompat.START)
            }
            else{
                binding.dlMain.openDrawer(GravityCompat.START)
            }
        }
        binding.nvMenu.setNavigationItemSelectedListener {
            binding.dlMain.closeDrawer(GravityCompat.START)
            when(it.itemId){
                R.id.menu_song -> binding.vpMusic.setCurrentItem(0)
                R.id.menu_album -> binding.vpMusic.setCurrentItem(1)
                R.id.menu_artist -> binding.vpMusic.setCurrentItem(2)
            }
            true
        }
    }
}