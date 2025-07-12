package com.example.hnote

//var playing = 0

var CurrentSong = "asset:///immortal-smoke.mp3"
var audioStopped = 0
var playing = 0


object PlaylistStore {
    var currentPlaylistName: String = ""
    var currentSongs: List<Pair<String, String>> = listOf()
}

var TimerCoroutineStopper=0

var CoroutinesCount=0