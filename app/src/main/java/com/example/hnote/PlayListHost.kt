package com.example.hnote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlayListHost.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayListHost : Fragment(R.layout.fragment_play_list_host) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.playListHostContainer, PlayLists()) // 👈 Same ID as in XML
                .commit()
        }
    }

    fun openPlaylistDetail(playlist: PlayList) {
        val fragment = MusicsFragment.newInstance(playlist.name, playlist.MusicList)
        childFragmentManager.beginTransaction()
            .replace(R.id.playListHostContainer, fragment)  // 👈 Same ID again
            .addToBackStack(null)
            .commit()
    }
}