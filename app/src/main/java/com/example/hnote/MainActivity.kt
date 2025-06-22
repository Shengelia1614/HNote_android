package com.example.hnote


import android.media.audiofx.Visualizer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
//import androidx.media3.datasource.AssetDataSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource

import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.AssetDataSource
import androidx.media3.datasource.DefaultDataSource

import com.example.hnote.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

import androidx.media3.exoplayer.ExoPlayer
import androidx.viewpager2.widget.ViewPager2


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pagerAdapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pagerAdapter = Adapter(this)
        binding.viewPager.adapter = pagerAdapter

        // Disable swipe animations when changing via BottomNav (optional)
        binding.viewPager.isUserInputEnabled = true

        // BottomNavigation item selected listener → change ViewPager page
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_play -> binding.viewPager.currentItem = 0
                R.id.nav_playlists -> binding.viewPager.currentItem = 1
                R.id.nav_files -> binding.viewPager.currentItem = 2
            }
            true
        }

        // When swiping with ViewPager, update BottomNavigationView selection
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
    }
    @OptIn(UnstableApi::class)
    private fun initializeApp() {
        Log.d("my tag","why is this not happening")

        val player = ExoPlayer.Builder(this).build()
        val mediaItem = MediaItem.fromUri("asset:///immortal-smoke.mp3")
        val dataSourceFactory = DefaultDataSource.Factory(this)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)

        player.setMediaSource(mediaSource)
        player.prepare()
        player.play()
        val visualizer = Visualizer(player.audioSessionId).apply {
            captureSize = Visualizer.getCaptureSizeRange()[1]
            setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(
                    visualizer: Visualizer, bytes: ByteArray, samplingRate: Int
                ) {
                    // Use this data for waveform visualization
                }

                override fun onFftDataCapture(
                    visualizer: Visualizer, bytes: ByteArray, samplingRate: Int
                ) {
                    // Use this FFT data to display frequency response
                    for (i in bytes){

                    }
                }
            }, Visualizer.getMaxCaptureRate() / 2, true, true)
            enabled = true
        }

    }


}



