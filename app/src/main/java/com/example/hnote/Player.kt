package com.example.hnote


import android.media.audiofx.Visualizer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hnote.databinding.FragmentPlayerBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Player.newInstance] factory method to
 * create an instance of this fragment.
 */



class Player  : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_TEXT = "arg_text"
        fun newInstance(text: String): Player {
            val fragment = Player()
            fragment.arguments = Bundle().apply {
                putString(ARG_TEXT, text)
            }
            return fragment
        }
        var playing = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val adapter =Adapter(requireActivity())
//        binding.layout1.adapter = adapter
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = systemBarsInsets.bottom)
            insets
        }


        val audio=Audio(requireContext())

        binding.play.setOnClickListener {
            if(playing==0){
                playing = 1
                audio.player.play()
                binding.play.setImageResource(R.drawable.pausebutton_bc)


            }else{
                playing=0
                audio.player.pause()
                binding.play.setImageResource(R.drawable.playbutton_bc)
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @OptIn(UnstableApi::class)
    private fun AudioPlayer(): ExoPlayer {
        Log.d("my tag","why is this not happening")

        val player = ExoPlayer.Builder(requireContext()).build()
        val mediaItem = MediaItem.fromUri("asset:///immortal-smoke.mp3")
        val dataSourceFactory = DefaultDataSource.Factory(requireContext())
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)

        player.setMediaSource(mediaSource)
        player.prepare()

        return player
//        if(option==0) {
//            player.play()
////            val visualizer = Visualizer(player.audioSessionId).apply {
////                captureSize = Visualizer.getCaptureSizeRange()[1]
////                setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
////                    override fun onWaveFormDataCapture(
////                        visualizer: Visualizer, bytes: ByteArray, samplingRate: Int
////                    ) {
////                        // Use this data for waveform visualization
////                    }
////
////                    override fun onFftDataCapture(
////                        visualizer: Visualizer, bytes: ByteArray, samplingRate: Int
////                    ) {
////                        // Use this FFT data to display frequency response
////                        for (i in bytes){
////
////                        }
////                    }
////                }, Visualizer.getMaxCaptureRate() / 2, true, true)
////                enabled = true
////            }
//        }else{
//            player.stop()
//        }


    }
}