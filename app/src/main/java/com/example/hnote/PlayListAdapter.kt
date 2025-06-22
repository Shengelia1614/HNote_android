package com.example.hnote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayListAdapter (
    private val playlists: Play_Lists,
    private val onClick: (PlayList) -> Unit
) : RecyclerView.Adapter<PlayListAdapter.MusicViewHolder>() {

    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.Playlist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_example, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val playlist = playlists.playlists[position]
        holder.title.text = playlist.
        holder.itemView.setOnClickListener { onClick(song) }
    }

    override fun getItemCount() = playlists.playlists.size
}