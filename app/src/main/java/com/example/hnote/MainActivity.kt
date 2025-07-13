package com.example.hnote


import android.content.pm.PackageManager
import android.media.audiofx.Visualizer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils.split
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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
//import com.example.hnote.Player.Companion.visualizer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.hypot


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pagerAdapter: Adapter
    lateinit var animationController: AnimationController
    lateinit public var audio : Audio
    var visualizer: Visualizer? = null

    companion object{
        public lateinit var Play_lists_db: MusicDataBaseController

    }
    private val REQUEST_CODE = 100


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        animationController = AnimationController()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pagerAdapter = Adapter(this)
        binding.viewPager.adapter = pagerAdapter

        binding.viewPager.offscreenPageLimit = 3
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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ uses READ_MEDIA_AUDIO
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_AUDIO), REQUEST_CODE)
            }
        } else {
            // Android 12 and below use READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
            }
        }


        Play_lists_db = MusicDataBaseController(this)

        audio =Audio(this)


        var allsongs= PlayList("all_songs",getAllAudioFilePaths())
        Log.d("db creation", "music count ${allsongs.MusicList.size} ")
        Play_lists_db.createTableFromList(allsongs)


        lifecycleScope.launch {
            while (true) {
                animationController.updateRotationProgress()
                animationController.updateProgress(audioPlayerDuration,progressTrackwidth,8)
                //pagerAdapter.player.getBind().cassetteRotation1.rotation = animationController.rotationAngle
                //pagerAdapter.player.getBind().cassetteRotation2.rotation = animationController.rotationAngle
                //animationController.updateProgress(pagerAdapter.player.)
                delay(16)
            }
        }


    }

    @UnstableApi
    fun startFrequencyAnalyzer() {
        val sessionId = audio.player.audioSessionId
        if (sessionId == 0) {
            Log.w("Visualizer", "Session ID is 0, cannot start Visualizer yet.")
            return
        } // no audio session yet

        visualizer?.release() // release if already exists

        visualizer = Visualizer(sessionId).apply {
            captureSize = Visualizer.getCaptureSizeRange()[1] // max capture size

            setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                override fun onFftDataCapture(
                    visualizer: Visualizer,
                    fft: ByteArray,
                    samplingRate: Int
                ) {
                    val n = 20
                    val bucketSize = (fft.size / 2) / n
                    val amps = FloatArray(n) { 0f }

                    // FFT bytes: pairs of (real, imag) starting from index 2 (index 0 and 1 reserved)
                    for (i in 0 until n) {
                        var sum = 0f
                        for (j in 0 until bucketSize) {
                            val index = 2 + (i * bucketSize + j) * 2
                            if (index + 1 >= fft.size) break
                            val real = fft[index].toInt()
                            val imag = fft[index + 1].toInt()
                            val mag = hypot(real.toFloat(), imag.toFloat())
                            sum += mag
                        }
                        amps[i] = sum / bucketSize // average magnitude in bucket
                    }

                    amps[0] = 0f

                    val minAmp = amps.minOrNull() ?: 0f
                    val maxAmp = amps.maxOrNull() ?: 1f
                    val range = (maxAmp - minAmp).takeIf { it != 0f } ?: 1f  // avoid divide by 0

                    for (k in amps.indices) {
                        amps[k] = ((amps[k] - minAmp) / range) * 7f + 1f  // scaled to [1, 8]
                    }


                    Log.d("frequency_source","applaying frequency ${amps[2]}")
                    latestAmplitudes = amps
                }

                override fun onWaveFormDataCapture(
                    visualizer: Visualizer,
                    waveform: ByteArray,
                    samplingRate: Int
                ) {
                    // Not used here
                }

            }, Visualizer.getMaxCaptureRate() / 2, false, true)

            enabled = true
        }
    }

    fun stopFrequencyAnalyzer() {
        visualizer?.release()
        visualizer = null
    }

    // Call this anytime to get latest 20-band amplitudes scaled 0..2
    fun getLatestFrequencyAmplitudes(): FloatArray {
        return latestAmplitudes.copyOf()
    }





    fun getAllAudioFilePaths(): List<Pair<String, String>> {
//        val audioList = mutableListOf<String>()
//
//        val projection = arrayOf(
//            MediaStore.Audio.Media.DATA // Deprecated but still works for path
//        )
//
//        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
//
//        this.contentResolver.query(
//            uri,
//            projection,
//            selection,
//            null,
//            null
//        )?.use { cursor ->
//            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
//            while (cursor.moveToNext()) {
//                val path = cursor.getString(dataColumn)
//                audioList.add(path)
//            }
//        }
//
//        return audioList
//        val audioList = mutableListOf<Pair<String, String>>()
//
//        val projection = arrayOf(
//            MediaStore.Audio.Media.DATA // deprecated but works
//        )
//
//        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
//
//        this.contentResolver.query(
//            uri,
//            projection,
//            selection,
//            null,
//            null
//        )?.use { cursor ->
//            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
//            while (cursor.moveToNext()) {
//                val path = cursor.getString(dataColumn)
//                val name=split(path,"/")
//                audioList.add(Pair<String, String>(name[name.size-1],path))
//            }
//        }
//
//        return audioList


        val audioList = mutableListOf<Pair<String, String>>()

        val projection = arrayOf(
            MediaStore.Audio.Media.DISPLAY_NAME,  // file name
            MediaStore.Audio.Media.DATA           // file path (deprecated but still works)
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        val query = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        query?.use { cursor ->
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (cursor.moveToNext()) {
                val name = cursor.getString(nameColumn)
                val path = cursor.getString(dataColumn)
                audioList.add(name to path)
            }
        }

        Log.d("MainActivity", "Found ${audioList.size} audio files")
        // Use audioList as needed, e.g. update UI or database
        return audioList
    }

    public fun GetPlayer(): Player{
        return pagerAdapter.player
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



