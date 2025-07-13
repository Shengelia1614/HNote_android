package com.example.hnote


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.metadata.MetadataOutput
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.extractor.metadata.id3.ApicFrame
import com.example.hnote.MainActivity.Companion.Play_lists_db
import androidx.media3.common.Player as MediaPlayer

class Audio @OptIn(UnstableApi::class) constructor
    (private val context: Context) {
    public var player : ExoPlayer

    var SongArtBitmap: Bitmap? = null

    init {
        Log.d("my tag","why is this not happening")

        player = ExoPlayer.Builder(context).build()
        val uriString = Play_lists_db.getLastPlayed() ?: "asset:///immortal-smoke.mp3"

        val mediaItem = MediaItem.fromUri(uriString)
        val dataSourceFactory = DefaultDataSource.Factory(context)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)

        player.addListener(object : MediaPlayer.Listener {
            override fun onMediaMetadataChanged(metadata: MediaMetadata) {
                val artworkData = metadata.artworkData
                if (artworkData != null) {
                    SongArtBitmap = BitmapFactory.decodeByteArray(
                        artworkData,
                        0,
                        artworkData.size
                    )
                    Log.d("Audio", "Loaded artwork bitmap: ${SongArtBitmap != null}")
                } else {
                    Log.d("Audio", "No artwork found in metadata.")
                }
            }
        })



        player.setMediaSource(mediaSource)
        player.prepare()

    }

    @OptIn(UnstableApi::class)
    public fun setAudio(){
        val mediaItem = MediaItem.fromUri(Play_lists_db.getLastPlayed() ?: "asset:///immortal-smoke.mp3")
        val dataSourceFactory = DefaultDataSource.Factory(context)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)

        player.setMediaSource(mediaSource)
        player.prepare()
    }
}