package com.example.hnote


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.contextaware.ContextAware
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hnote.Player.Companion.audio
//import com.example.hnote.databinding.FragmentPlayListsBinding
import com.example.hnote.databinding.FragmentPlayerBinding
import com.example.hnote.databinding.ItemMusicBinding


class MusicAdapter(private val songs: List<String>, val ac_context: Context) : RecyclerView.Adapter<MusicAdapter.SongViewHolder>() {
    inner class SongViewHolder(val binding: ItemMusicBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        Log.d("MusicAdapter", "Binding item")
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        Log.d("MusicAdapter", "Binding position: $position → ${songs[position]}")

        holder.binding.musicList.text = songs[position]
        holder.binding.root.setOnClickListener {
            CurrentSong=songs[position]
            Player.audio.player.stop()

            audio =Audio(ac_context)
            Player.audio.player.play()

            playing=1

        }

    }

    override fun getItemCount() = songs.size
}