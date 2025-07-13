package com.example.hnote

import android.os.SystemClock

class AnimationController {
    var isRotationPlaying = false
    var rotationprogress = 0f // e.g., 0..1 for animation progress
    var rotationAngle = 0f

    var isProgressPlaying =false
    var Progress =0f



    private var lastProgressUpdate=0L
    private var lastRotationUpdateTime = 0L


    //starts


    fun startRotation() {
        isRotationPlaying = true
        lastRotationUpdateTime = SystemClock.elapsedRealtime()
    }
    fun startProgress(){
        isProgressPlaying =true
        lastProgressUpdate = SystemClock.elapsedRealtime()
    }



    //pauses

    fun pauseRotation() {
        updateRotationProgress()
        isRotationPlaying = false
    }
    fun pauseProgress(){
        //updateProgress(duration,destination,originalSize)
        isProgressPlaying =false
    }



    //updates

    fun updateProgress(duration: Long, destination: Int,originalSize: Int){
        if(isProgressPlaying){
            val now = SystemClock.elapsedRealtime()
            val delta = now - lastProgressUpdate

            val progressDelta = (delta.toFloat() / duration) * (destination-24)
            Progress = (Progress + progressDelta).coerceAtMost((destination-24).toFloat())

            lastProgressUpdate = now

        }
    }

    fun updateRotationProgress() {
        if (isRotationPlaying) {
            val now = SystemClock.elapsedRealtime()
            val delta = now - lastRotationUpdateTime
            // update rotation angle or progress according to delta
            rotationAngle = (rotationAngle + delta * 0.072f) % 360f  // 360deg / 5000ms = 0.072deg/ms
            rotationprogress += delta.toFloat() / 500
            if (rotationprogress > 1f) rotationprogress = 1f
            lastRotationUpdateTime = now
        }
    }

    //reset

    fun resetProgress(){
        Progress=0f
        lastProgressUpdate=0
        isProgressPlaying =false
        isRotationPlaying = false
    }
}
