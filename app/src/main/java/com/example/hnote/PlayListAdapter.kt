package com.example.hnote

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hnote.MusicsFragment
import com.example.hnote.databinding.ItemPlaylistBinding

class PlaylistAdapter(
    private val playlists: List<PlayList>,
    private val onItemClick: (PlayList) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.binding.playlistName.text = playlist.name
        holder.binding.root.setOnClickListener {
            Log.d("PlaylistAdapter", "Clicked playlist: ${playlist.name}")
            onItemClick(playlist) }
    }

    override fun getItemCount() = playlists.size
}