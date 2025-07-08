package com.example.hnote


import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.hnote.MainActivity.Companion.Play_lists_db

class Audio @OptIn(UnstableApi::class) constructor
    (private val context: Context) {
    public var player : ExoPlayer

    init {
        Log.d("my tag","why is this not happening")

        player = ExoPlayer.Builder(context).build()
        val uriString = Play_lists_db.getLastPlayed() ?: "asset:///immortal-smoke.mp3"

        val mediaItem = MediaItem.fromUri(uriString)
        val dataSourceFactory = DefaultDataSource.Factory(context)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)

        player.setMediaSource(mediaSource)
        player.prepare()

    }

    @OptIn(UnstableApi::class)
    public fun setAudio(path: String){
        val mediaItem = MediaItem.fromUri(path)
        val dataSourceFactory = DefaultDataSource.Factory(context)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
    }
}