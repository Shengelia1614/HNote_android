package com.example.hnote

import android.content.Context

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils.split
import com.example.hnote.PlayList

class MusicDataBaseController(context: Context) :SQLiteOpenHelper(context, "Playlists.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createPlaylistsTable = """
        CREATE TABLE IF NOT EXISTS playlists (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT UNIQUE NOT NULL
        )
    """.trimIndent()
        db.execSQL(createPlaylistsTable)

        val creatConfigVariablesTable = """
        CREATE TABLE IF NOT EXISTS configvariables (
            Identifier TEXT PRIMARY KEY,
            Value TEXT  NOT NULL
        )
    """.trimIndent()
        db.execSQL(creatConfigVariablesTable)

        val table = sanitize("configvariables")
        val insertSQL = """
            INSERT INTO $table (Identifier, Value)
            VALUES (?, ?)
        """.trimIndent()

        db.execSQL(insertSQL, arrayOf("LastPlayed", CurrentSong))
    }

    fun updateLastPlayed(LastPlayed: String) {
        val sql = "UPDATE configvariables SET Value = ? WHERE Identifier = ?"


        writableDatabase.execSQL(sql, arrayOf(LastPlayed, "LastPlayed"))
        writableDatabase.execSQL(sql, arrayOf(LastPlayed, "LastPlayed"))
    }


    fun getLastPlayed(): String? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT Value FROM configvariables WHERE Identifier = ?", arrayOf("LastPlayed"))

        var path: String? = null
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndexOrThrow("Value"))
        }

        cursor.close()
        return path
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle upgrades if needed
    }

    fun createTableFromList( pls: PlayList){
        createPlaylistTable(pls.name)

        for(music in pls.MusicList){
            //val name=split(music,"/")
            insertAudio(pls.name,music.first,music.second)
        }
    }

    fun addPlaylistName(playlistName: String) {
        val sanitized = sanitize(playlistName)
        val insertSQL = "INSERT OR IGNORE INTO playlists (name) VALUES (?)"
        writableDatabase.execSQL(insertSQL, arrayOf(sanitized))
    }

    // Create new playlist table
    fun createPlaylistTable(playlistName: String) {

        val table = sanitize(playlistName)
        addPlaylistName(playlistName)
        val createSQL = """
            CREATE TABLE IF NOT EXISTS $table (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                audioName TEXT,
                audioPath TEXT
            )
        """.trimIndent()

        writableDatabase.execSQL(createSQL)
    }

    // Insert song into playlist
    fun insertAudio(playlistName: String, audioName: String, audioPath: String) {
        val table = sanitize(playlistName)
        val insertSQL = """
            INSERT INTO $table (audioName, audioPath)
            VALUES (?, ?)
        """.trimIndent()

        writableDatabase.execSQL(insertSQL, arrayOf(audioName, audioPath))
    }


    // Read songs from playlist
    fun getSongs(playlistName: String): List<Pair<String, String>> {
        val table = sanitize(playlistName)
        val cursor = readableDatabase.rawQuery("SELECT audioName, audioPath FROM $table", null)

        val list = mutableListOf<Pair<String, String>>()
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(0)
                val path = cursor.getString(1)
                list.add(name to path)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getAllTableNames(): List<String> {
        val tables = mutableListOf<String>()
        val cursor = readableDatabase.rawQuery("SELECT name FROM playlists", null)
        if (cursor.moveToFirst()) {
            do {
                val tableName = cursor.getString(0)
                tables.add(tableName)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tables
    }

    fun getAllPlaylistsWithSongs(): List<Pair<String, List<Pair<String, String>>>> {
        val playlists = mutableListOf<Pair<String, List<Pair<String, String>>>>()

        val tables = getAllTableNames() // function from previous step

        for (table in tables) {
            val songs = getSongs(table) // your existing function
            playlists.add(table to songs)
        }

        return playlists
    }


    private fun sanitize(name: String): String {
        // Remove spaces/special chars to make safe table name
        return name.replace(Regex("[^A-Za-z0-9_]"), "_")
    }
}