package com.example.hnote

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hnote.databinding.FragmentMusicsBinding
import com.example.hnote.databinding.FragmentPlayListsBinding
import com.example.hnote.databinding.FragmentPlayerBinding

class MusicsFragment : Fragment() {
    private var _binding: FragmentMusicsBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlistName: String
    private lateinit var songs: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlistName = it.getString(ARG_NAME, "") ?: ""
            songs = it.getStringArrayList(ARG_SONGS)?.toList() ?: emptyList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMusicsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.title.text = playlistName
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = MusicAdapter(songs)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_NAME = "name"
        private const val ARG_SONGS = "songs"

        fun newInstance(name: String, songs: List<String>) = MusicsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_NAME, name)
                putStringArrayList(ARG_SONGS, ArrayList(songs))
            }
        }
    }
}