package com.example.hnote

import android.animation.ObjectAnimator
import android.animation.ValueAnimator

//var playing = 0

var CurrentSong = "asset:///immortal-smoke.mp3"
var CurrentPosition=1
var audioStopped = 0
var playing = 0
var frequencyPlayer=0

var audioPlayerDuration=0L
var progressTrackwidth = 0

var latestAmplitudes = FloatArray(20) { 0f }

object PlaylistStore {
    var currentPlaylistName: String = ""
    var currentSongs: List<Pair<String, String>> = listOf()
}

object Animations {
    lateinit public var progress: ObjectAnimator
    lateinit public var progresingbar: ValueAnimator
    lateinit public var rotation1: ObjectAnimator
    lateinit public var rotation2: ObjectAnimator
}




var TimerCoroutineStopper=0

var CoroutinesCount=0