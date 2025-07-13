package com.example.hnote


import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.media.audiofx.Visualizer
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils.split
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import androidx.media3.common.Player as MediaPlayer
import kotlin.math.hypot
import kotlin.math.min


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
    public val  mainActivity: MainActivity
        get() = requireActivity() as MainActivity


    var playtime=0L
    var onPauseTime=0L



       val bars = mutableListOf<ImageView>()
    //val mainScope = (activity as? MainActivity)?.lifecycleScope


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
        //lateinit public var progress: ObjectAnimator
        //lateinit public var progresingbar: ValueAnimator
        //lateinit public var rotation1: ObjectAnimator
        //lateinit public var rotation2: ObjectAnimator
        //lateinit var mainActivity : Context
        private var visualizer: Visualizer? = null

        var myJob: Job? = null
        var myFrequencyJob: Job? = null


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

                        //progress = ObjectAnimator.ofFloat(binding.progressCircle, "translationX", 0f, t2)
                        //progress.duration = audio.player.duration
                        binding.progressTrack.post{
                            //progress.start()
                            //progress.pause()
                        }

                        Log.d("progress", "start from $t1 to $temp")
                    }
                    binding.progressingTrack.post {

//                        var temp2 = (binding.progressTrack.x + binding.progressTrack.width.toFloat()).toFloat() / 8f
//
//
//                        progresingbar = ObjectAnimator.ofFloat(binding.progressingTrack, "scaleX", 1f, temp2)
//                        progresingbar.duration = audio.player.duration
//                        progresingbar = ValueAnimator.ofInt(0, binding.progressTrack.width)
//                        progresingbar.duration = audio.player.duration
//                        progresingbar.addUpdateListener { animation ->
//                            val animatedValue = animation.animatedValue as Int
//
//                            val layoutParams = binding.progressingTrack.layoutParams
//                            layoutParams.width = animatedValue
//                            binding.progressingTrack.layoutParams = layoutParams
//                        }
//
//                        progresingbar.start()
//                        progresingbar.pause()
                    }
//                    binding.cassetteRotation1.post {
//
//                        rotation1 =
//                            ObjectAnimator.ofFloat(binding.cassetteRotation1, "rotation", 0f, 360f)
//                                .apply {
//                                    duration = 5000 // 1 second per rotation
//                                    repeatCount = ObjectAnimator.INFINITE
//                                    interpolator = LinearInterpolator()
//                                    start()
//                                }
//
//                        rotation1.pause()
//
//                    }

//                    binding.cassetteRotation2.post {
//                        rotation2 = ObjectAnimator.ofFloat(binding.cassetteRotation2, "rotation", 0f, 360f).apply {
//                            duration = 5000 // 1 second per rotation
//                            repeatCount = ObjectAnimator.INFINITE
//                            interpolator = LinearInterpolator()
//                            start()
//                        }
//                        rotation2.pause()
//                    }

                    binding.imageDisplayScreen.post{

                        if (audio.SongArtBitmap != null) {
                            binding.SongImage.setImageBitmap(audio.SongArtBitmap)
                        } else {
                            Log.w("debug", "No album art found.")
                            // Replace with your actual drawable
                        }
                    }
                    binding.frequencyDisplay.post{


                        for (i in 0..19){
                            val resId = resources.getIdentifier("bar$i", "id", requireContext().packageName)
                            val bar = requireView().findViewById<ImageView>(resId)
                            bars.add(bar)
                        }

                    }


//                    if(audioStopped==1){
//                        //audio =Audio(requireActivity())
//                        audioStopped=0
//                        //if (::progress.isInitialized) {
//
//
//                        //}
//                    }
                    Log.d("debuging","starting listener")

                    audioPlayerDuration=audio.player.duration
                    progressTrackwidth=binding.progressTrack.width

                    binding.play.setOnClickListener {

                        Log.d("debuging","playing state when clicked $playing")
                        if(playing==0){
                            val mainScope = (activity as? MainActivity)?.lifecycleScope ?: lifecycleScope
                            Play_update(mainScope)

//                            playing = 1
//                            audio.player.play()
//                            //if (::progress.isInitialized) {
//
//                            progress.resume()a
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
                            //rotation1.pause()
                            //rotation2.pause()
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

    @UnstableApi
    fun startFrequencyAnalyzer() {
        val sessionId = audio.player.audioSessionId
        if (sessionId == 0) return // no audio session yet

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


    fun applyFequencyVisuals(){

        var visuals=getLatestFrequencyAmplitudes()
        Log.d("frequency","applaying frequency ${visuals[2]}")
        if (bars.size < 20) return
        for (i in 0..19){
            bars.get(i).scaleY=visuals[i]
        }

    }

    fun resumeAnimation() {
        //Companion.rotation1.resume()
    }

    fun pauseAnimation() {
        //Companion.rotation1.pause()
    }

    fun applyRotationAnimations(animationController: AnimationController) {
        animationController.updateRotationProgress()
        binding.cassetteRotation1.rotation = animationController.rotationAngle
        binding.cassetteRotation2.rotation = animationController.rotationAngle
        Log.d("Rotation", "angle=${animationController.rotationAngle}")

        // Apply progress to progress bar or other views here
    }
    fun applyProgressAnimations(animationController: AnimationController) {
        animationController.updateProgress(audio.player.duration,binding.progressTrack.width,8)
        binding.progressingTrack.pivotX=0f
        //binding.progressingTrack.scaleX=animationController.Progressingbar
        val layoutParams = binding.progressingTrack.layoutParams
        layoutParams.width=animationController.Progress.toInt()+12
        binding.progressingTrack.layoutParams=layoutParams
        binding.progressCircle.translationX=animationController.Progress

        //Log.d("Rotation", "angle=${animationController.rotationAngle}")

        // Apply progress to progress bar or other views here
    }


    public fun Play_update(scope: CoroutineScope){

        //Log.d("animations","${rotation1.isPaused}, ${rotation2.isPaused}, ${progress.isPaused}, ${progresingbar.isPaused}")

        playing = 1
        frequencyPlayer=1
        audio.player.play()
        //if (::progress.isInitialized) {

        //progress.resume()
        Log.d("progress", "animation started")
        //}
        //if (::progresingbar.isInitialized) {

        //progresingbar.resume()
        Log.d("progress", "animation started")
        //}

        var playbackStartTime = SystemClock.elapsedRealtime()

        mainActivity?.animationController?.startRotation()
        mainActivity.animationController.startProgress()

        @UnstableApi
        startFrequencyAnalyzer()

        Log.d("coroutines","$CoroutinesCount")

        myFrequencyJob= scope.launch {
            Log.d("debuging", "myFrequencyJob starting: bars=${bars.size}, amplitudes=${getLatestFrequencyAmplitudes().joinToString()}, access $frequencyPlayer")
            try {
                while (isActive) {
                    Log.d("CRoutine","coroutine active, $frequencyPlayer")
                    while (frequencyPlayer == 1) {
                        Log.d("debug", "Applying visuals")
                        applyFequencyVisuals()

                        //applyRotationAnimations(mainActivity?.animationController!!)
                        //mainActivity.animationController.updateRotationProgress()
                        binding.cassetteRotation1.rotation = mainActivity.animationController.rotationAngle
                        binding.cassetteRotation2.rotation = mainActivity.animationController.rotationAngle
                        Log.d("Rotation", "angle=${mainActivity.animationController.rotationAngle}")
                        //applyProgressAnimations(mainActivity.animationController)


                        //mainActivity.animationController.updateProgress(,binding.progressTrack.width,8)
                        binding.progressingTrack.pivotX=0f
                        //binding.progressingTrack.scaleX=animationController.Progressingbar
                        val layoutParams = binding.progressingTrack.layoutParams
                        layoutParams.width=mainActivity.animationController.Progress.toInt()+12
                        binding.progressingTrack.layoutParams=layoutParams
                        binding.progressCircle.translationX=mainActivity.animationController.Progress

                        delay(1)
                    }
                }
            } catch (e: Exception) {
                Log.e("debuging", "Exception in myFrequencyJob", e)
            }finally {
                stopFrequencyAnalyzer()
            }
        }
        myJob = scope.launch {

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




                    Log.d("Coroinside-PlayingState","$playing")
                    playing=0
                    binding.play.setImageResource(R.drawable.playbutton_bc)


                    mainActivity.animationController.resetProgress()
                    mainActivity.animationController.pauseRotation()

                    //progress.setCurrentPlayTime(0)
                    //progress.pause()
                    //progresingbar.setCurrentPlayTime(0)
                    //progresingbar.pause()
                    //rotation1.pause()
                    //rotation2.pause()
                    playtime=0
                    binding.timepast.text= String.format("%d:%02d", (playtime/60000).toLong(),  ((playtime%60000)/1000).toLong())
                    onPauseTime=0



                    delay(1000)
                }
            } finally {

                playing=0
                CoroutinesCount -=1
                Log.d("Coroutine", "Cleaned up after cancel")
            }

        }
        Log.d("coroutines","$CoroutinesCount")
        Log.d("Coro-PlayingState","$CoroutinesCount")

        binding.play.setImageResource(R.drawable.pausebutton_bc)


    }
    public fun Stop_update(){

        mainActivity?.animationController?.pauseRotation()
        mainActivity.animationController.pauseProgress()
        Log.d("debuging","stoping sound")
        //rotation1.pause()
        //rotation2.pause()
        playing=0
        frequencyPlayer=0
        audio.player.pause()
        //if (::progress.isInitialized) {
        //progress.pause()
        //}
        audio.player.pause()
        //if (::progresingbar.isInitialized) {
        //progresingbar.pause()
        //}


        myJob?.cancel()
        myFrequencyJob?.cancel()
        //stopFrequencyAnalyzer()
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