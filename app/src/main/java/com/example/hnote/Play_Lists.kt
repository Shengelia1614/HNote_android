package com.example.hnote

import android.content.Context


class Play_Lists(private val context: Context) {
    lateinit var playlists: MutableList<PlayList>
    init {
        //playlists.add(PlayList(context))
    }

    fun addPlayList_ViaFolder(folderPath: String){
        //playlists.add(PlayList(context,folderPath))
    }
}