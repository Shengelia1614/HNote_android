package com.example.hnote

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hnote.databinding.FragmentPlayListsBinding
import com.example.hnote.databinding.FragmentPlayerBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlayLists.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayLists : Fragment() {
    private var _binding: FragmentPlayListsBinding? = null
    private val binding get() = _binding!!


    companion object {
        private const val ARG_TEXT = "arg_text"
        fun newInstance(text: String): PlayLists {
            val fragment = PlayLists()
            fragment.arguments = Bundle().apply {
                putString(ARG_TEXT, text)
            }
            return fragment
        }

    }

    private lateinit var playlists: List<PlayList>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        playlists = listOf(PlayList(requireContext()))

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = PlaylistAdapter(playlists) { playlist ->
            openPlaylistDetail(playlist)
        }
    }

    private fun openPlaylistDetail(playlist: PlayList) {
        Log.d("PlayListsFragment", "openPlaylistDetail called with: ${playlist.name}")

        (parentFragment as? PlayListHost)?.openPlaylistDetail(playlist)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}