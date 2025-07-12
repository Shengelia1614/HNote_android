package com.example.hnote


import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils.split
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.hnote.MainActivity.Companion.Play_lists_db
import com.example.hnote.databinding.FragmentPlayerBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import androidx.media3.common.Player as MediaPlayer


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

    var playtime=0L
    var onPauseTime=0L
    var myJob: Job? = null

    public fun getBind(): FragmentPlayerBinding{
        return binding
    }

    val contxt: Context
        get() = requireContext()

    companion object {
        private const val ARG_TEXT = "arg_text"
        fun newInstance(text: String): Player {
            val fragment = Player()
            fragment.arguments = Bundle().apply {
                putString(ARG_TEXT, text)
            }
            return fragment
        }

        lateinit public var audio : Audio
        lateinit public var progress: ObjectAnimator
        lateinit public var progresingbar: ValueAnimator
        lateinit public var rotation1: ObjectAnimator
        lateinit public var rotation2: ObjectAnimator
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


        audio =Audio(requireActivity())
        //val Trackwidth= IntArray(2)


        val name=split(Play_lists_db.getLastPlayed(),"/")
        binding.musicname.text=name[name.size-1]

        audio.player.addListener(object : MediaPlayer.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == MediaPlayer.STATE_READY) {
                    val marqueeText = view.findViewById<TextView>(R.id.musicname)
                    marqueeText.isSelected = true



                    binding.progressCircle.post {


                        binding.progressCircle.translationX = 0f
                        val t1 = binding.progressCircle.x
                        val temp=binding.progressTrack.x
                        val t2 = binding.progressTrack.width.toFloat()-binding.progressCircle.width
                        Log.d("progress", "Animator from $t1 to $t2")

                        progress = ObjectAnimator.ofFloat(binding.progressCircle, "translationX", 0f, t2)
                        progress.duration = audio.player.duration
                        binding.progressTrack.post{
                            progress.start()
                            progress.pause()
                        }

                        Log.d("progress", "start from $t1 to $temp")
                    }
                    binding.progressingTrack.post {

//                        var temp2 = (binding.progressTrack.x + binding.progressTrack.width.toFloat()).toFloat() / 8f
//
//
//                        progresingbar = ObjectAnimator.ofFloat(binding.progressingTrack, "scaleX", 1f, temp2)
//                        progresingbar.duration = audio.player.duration
                        progresingbar = ValueAnimator.ofInt(0, binding.progressTrack.width)
                        progresingbar.duration = audio.player.duration
                        progresingbar.addUpdateListener { animation ->
                            val animatedValue = animation.animatedValue as Int

                            val layoutParams = binding.progressingTrack.layoutParams
                            layoutParams.width = animatedValue
                            binding.progressingTrack.layoutParams = layoutParams
                        }

                        progresingbar.start()
                        progresingbar.pause()
                    }
                    binding.cassetteRotation1.post {

                        rotation1 =
                            ObjectAnimator.ofFloat(binding.cassetteRotation1, "rotation", 0f, 360f)
                                .apply {
                                    duration = 5000 // 1 second per rotation
                                    repeatCount = ObjectAnimator.INFINITE
                                    interpolator = LinearInterpolator()
                                    start()
                                }

                        rotation1.pause()

                    }

                    binding.cassetteRotation2.post {
                        rotation2 = ObjectAnimator.ofFloat(binding.cassetteRotation2, "rotation", 0f, 360f).apply {
                            duration = 5000 // 1 second per rotation
                            repeatCount = ObjectAnimator.INFINITE
                            interpolator = LinearInterpolator()
                            start()
                        }
                        rotation2.pause()
                    }

//                    if(audioStopped==1){
//                        //audio =Audio(requireActivity())
//                        audioStopped=0
//                        //if (::progress.isInitialized) {
//
//
//                        //}
//                    }

                    binding.play.setOnClickListener {

                        if(playing==0){
                            Play_update()

//                            playing = 1
//                            audio.player.play()
//                            //if (::progress.isInitialized) {
//
//                            progress.resume()
//                            Log.d("progress", "animation started")
//                            //}
//                            //if (::progresingbar.isInitialized) {
//
//                            progresingbar.resume()
//                            Log.d("progress", "animation started")
//                            //}
//
//                            var playbackStartTime = SystemClock.elapsedRealtime()
//                            TimerCoroutineStopper=0
//
//                            lifecycleScope.launch {
//
//
//                                while (playtime<audio.player.duration) {
//                                    if(TimerCoroutineStopper==1){
//                                        break
//                                    }
//                                    playtime = (SystemClock.elapsedRealtime() - playbackStartTime)+onPauseTime
//                                    //binding.timepast.text = "${(playtime/60000).toLong()}:${((playtime%60000)/1000).toLong()}"
//                                    binding.timepast.text= String.format("%d:%02d", (playtime/60000).toLong(),  ((playtime%60000)/1000).toLong())
//
//                                    delay(1000)
//                                }
//                                if(TimerCoroutineStopper==0){
//                                    playing=0
//                                    binding.play.setImageResource(R.drawable.playbutton_bc)
//                                    progress.start()
//                                    progress.pause()
//                                    progresingbar.start()
//                                    progresingbar.pause()
//                                    playtime=0
//                                    binding.timepast.text= String.format("%d:%02d", (playtime/60000).toLong(),  ((playtime%60000)/1000).toLong())
//                                    onPauseTime=0
//                                }
//                                onPauseTime=playtime
//
//                            }
//
//
//                            binding.play.setImageResource(R.drawable.pausebutton_bc)
//

                        }else{
                            Stop_update()
//                            playing=0
//                            audio.player.pause()
//                            //if (::progress.isInitialized) {
//                            progress.pause()
//                            //}
//                            audio.player.pause()
//                            //if (::progresingbar.isInitialized) {
//                            progresingbar.pause()
//                            //}
//                            TimerCoroutineStopper=1
//
//                            binding.play.setImageResource(R.drawable.playbutton_bc)
                        }

                    }



                    Log.d("Audio", "Player is ready!")
                }
            }
        })



    }

//    override fun onResume() {
//        if(playing==0){
//            binding.play.setImageResource(R.drawable.playbutton_bc)
//
//
//        }else{
//
//            binding.play.setImageResource(R.drawable.pausebutton_bc)
//        }
//        super.onResume()
//
//    }


    public fun Play_update(){
        rotation1.resume()
        rotation2.resume()
        playing = 1
        audio.player.play()
        //if (::progress.isInitialized) {

        progress.resume()
        Log.d("progress", "animation started")
        //}
        //if (::progresingbar.isInitialized) {

        progresingbar.resume()
        Log.d("progress", "animation started")
        //}

        var playbackStartTime = SystemClock.elapsedRealtime()

        Log.d("coroutines","$CoroutinesCount")


        myJob = lifecycleScope.launch {

            try {
                while (isActive) {

                    CoroutinesCount +=1

                    while (playtime<audio.player.duration) {

                        playtime = (SystemClock.elapsedRealtime() - playbackStartTime)+onPauseTime
                        //binding.timepast.text = "${(playtime/60000).toLong()}:${((playtime%60000)/1000).toLong()}"
                        binding.timepast.text= String.format("%d:%02d", (playtime/60000).toLong(),  ((playtime%60000)/1000).toLong())
                        Log.d("timer","$playtime")
                        delay(1000)
                        ensureActive()
                        //onPauseTime=playtime
                    }



                    playing=0
                    Log.d("Coroinside-PlayingState","$playing")
                    binding.play.setImageResource(R.drawable.playbutton_bc)
                    progress.start()
                    progress.pause()
                    progresingbar.start()
                    progresingbar.pause()
                    playtime=0
                    binding.timepast.text= String.format("%d:%02d", (playtime/60000).toLong(),  ((playtime%60000)/1000).toLong())
                    onPauseTime=0



                    delay(1000)
                }
            } finally {
                CoroutinesCount -=1
                Log.d("Coroutine", "Cleaned up after cancel")
            }

        }
        Log.d("coroutines","$CoroutinesCount")
        Log.d("Coro-PlayingState","$CoroutinesCount")

        binding.play.setImageResource(R.drawable.pausebutton_bc)


    }
    public fun Stop_update(){
        rotation1.pause()
        rotation2.pause()
        playing=0
        audio.player.pause()
        //if (::progress.isInitialized) {
        progress.pause()
        //}
        audio.player.pause()
        //if (::progresingbar.isInitialized) {
        progresingbar.pause()
        //}

        myJob?.cancel()
        onPauseTime=playtime
        //CoroutinesCount -=1
        binding.play.setImageResource(R.drawable.playbutton_bc)
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