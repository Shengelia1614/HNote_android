package com.example.hnote


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.media3.common.Player
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hnote.MainActivity.Companion.Play_lists_db
import com.example.hnote.Player.Companion.audio
import com.example.hnote.Player.Companion.progresingbar
import com.example.hnote.Player.Companion.progress
//import com.example.hnote.databinding.FragmentPlayListsBinding
import com.example.hnote.databinding.FragmentPlayerBinding
import com.example.hnote.databinding.ItemMusicBinding

import androidx.media3.common.Player as MediaPlayer

class MusicAdapter(private val songs: List<Pair<String, String>>, val ac_context: Context) : RecyclerView.Adapter<MusicAdapter.SongViewHolder>() {
    inner class SongViewHolder(val binding: ItemMusicBinding) : RecyclerView.ViewHolder(binding.root)

    val mainActivity = ac_context as? MainActivity


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        Log.d("MusicAdapter", "Binding item")
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        Log.d("MusicAdapter", "Binding position: $position → ${songs[position]}")

        holder.binding.musicList.text = songs[position].first
        holder.binding.root.setOnClickListener {
            Play_lists_db.updateLastPlayed(songs[position].second)

            audio.player.stop()

            audio =Audio(ac_context)




            audio.player.addListener(object : MediaPlayer.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == MediaPlayer.STATE_READY) {
                        progress.cancel()

                        progress.duration = audio.player.duration
                        progress.start()

                        Log.d("progress", "animation canceled")
                        //}
                        //if (::progresingbar.isInitialized){
                        progresingbar.cancel()

                        progresingbar.duration = audio.player.duration
                        progresingbar.start()


                    }

                }
            })




            //audio.player.play()
            //mainActivity?.GetPlayer()?.getBind()?.play?.setImageResource(R.drawable.pausebutton_bc)
            mainActivity?.GetPlayer()?.Stop_update()
            //mainActivity?.GetPlayer()?.myJob?.cancel()

            mainActivity?.GetPlayer()?.onPauseTime=0L
            mainActivity?.GetPlayer()?.playtime=0L
            mainActivity?.GetPlayer()?.getBind()?.musicname?.text=songs[position].first


            mainActivity?.GetPlayer()?.Play_update()
            Log.d("CoroutineProblem","$playing")

            if(playing==0){
                playing=1
            }
            //playing=1
            //audioStopped==1

        }

    }

    override fun getItemCount() = songs.size
}