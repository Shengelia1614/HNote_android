package com.example.hnote

import android.content.Context
import android.provider.MediaStore


class PlayList(private val context: Context, Folder: String="") {
    lateinit var MusicList: List<String>
    lateinit var name: String
    init {
        if(Folder==""){
            MusicList= getAllAudioFilePaths()
            name="all"
        }else{

            name="something"
        }
    }

    fun getAllAudioFilePaths(): List<String> {
        val audioList = mutableListOf<String>()

        val projection = arrayOf(
            MediaStore.Audio.Media.DATA // Deprecated but still works for path
        )

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"

        context.contentResolver.query(
            uri,
            projection,
            selection,
            null,
            null
        )?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (cursor.moveToNext()) {
                val path = cursor.getString(dataColumn)
                audioList.add(path)
            }
        }

        return audioList
    }
}