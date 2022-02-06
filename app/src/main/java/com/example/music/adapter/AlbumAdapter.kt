package com.example.music.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R
import com.example.music.databinding.ItemAlbumBinding
import com.example.music.databinding.ItemSongBinding
import com.example.music.model.Song

class AlbumAdapter(private val list_song: Array<Song>):RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemAlbumBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(song:Song){
            binding.tvSongname.text = song.song_name
            binding.tvArtistname.text = song.artist_name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View? = LayoutInflater.from(parent.context).inflate(R.layout.item_album,parent,false)
        return AlbumAdapter.ViewHolder(ItemAlbumBinding.bind(view!!))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list_song[position])
    }

    override fun getItemCount(): Int {
       return list_song.size
    }
}