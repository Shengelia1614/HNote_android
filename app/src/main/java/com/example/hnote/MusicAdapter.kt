package com.example.hnote


import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.contextaware.ContextAware
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hnote.MainActivity.Companion.Play_lists_db
//import com.example.hnote.Player.Companion.audio
//import com.example.hnote.Player.Companion.progresingbar
//import com.example.hnote.Player.Companion.progress
//import com.example.hnote.Player.Companion.rotation1
//import com.example.hnote.Player.Companion.rotation2
import com.example.hnote.databinding.FragmentPlayListsBinding
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

            mainActivity?.audio?.player?.stop()

            //audio =Audio(ac_context)
            mainActivity?.audio?.setAudio()

            val bitmap: Bitmap? = mainActivity?.audio?.SongArtBitmap
            if (bitmap != null) {
                mainActivity?.GetPlayer()?.getBind()?.SongImage?.setImageBitmap(bitmap)
            } else {
                Log.w("MusicAdapter", "No album art bitmap available for song ${songs[position].first}")
                // Optionally clear image or set a placeholder:

            }



            mainActivity?.audio?.player?.addListener(object : MediaPlayer.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == MediaPlayer.STATE_READY) {
                        //progress.cancel()
                        //progress.start()
                        //progress.pause()
                        //progress.setCurrentPlayTime(0)
                        //progress.duration = audio.player.duration


                        mainActivity?.animationController?.resetProgress()
                        mainActivity?.animationController?.pauseProgress()
                        audioPlayerDuration=mainActivity?.audio?.player?.duration!!
                        mainActivity?.animationController?.startProgress()
                        mainActivity?.animationController?.startRotation()

                        @UnstableApi
                        mainActivity?.startFrequencyAnalyzer()
                        Log.d("progress", "animation canceled")
                        //}
                        //if (::progresingbar.isInitialized){


                        //progresingbar.cancel()
                        //progresingbar.start()
                        //progresingbar.pause()
                        //progresingbar.setCurrentPlayTime(0)
                        //progresingbar.duration = audio.player.duration



                        //rotation1.cancel()
                        //rotation1.start()
                        //AnimationController.resume()

                    }

                }
            })




            //audio.player.play()
            mainActivity?.GetPlayer()?.getBind()?.play?.setImageResource(R.drawable.pausebutton_bc)
            //mainActivity?.GetPlayer()?.Stop_update()
            mainActivity?.audio?.player?.pause()
            //mainActivity?.GetPlayer()?.myJob?.cancel()


            //mainActivity?.GetPlayer()?.onPauseTime=0L
            //mainActivity?.GetPlayer()?.playtime=0L
            mainActivity?.GetPlayer()?.getBind()?.musicname?.text=songs[position].first


            frequencyPlayer=1

//            progress.start()
//            progresingbar.start()
//            rotation1.start()
//            rotation2.start()


            //mainActivity?.GetPlayer()?.Play_update((mainActivity as AppCompatActivity).lifecycleScope)
            mainActivity?.audio?.player?.play()
            playing = 1
            frequencyPlayer=1
            playtime=0
            onPauseTime=0
            playbackStartTime = SystemClock.elapsedRealtime()

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