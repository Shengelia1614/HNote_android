package com.example.hnote


import android.content.Context
import android.provider.MediaStore


class PlayList(name: String="", list: List<Pair<String, String>>) {
    lateinit var MusicList: List<Pair<String, String>>
    lateinit var name: String
    init {
        MusicList=list
        this.name=name
    }


}