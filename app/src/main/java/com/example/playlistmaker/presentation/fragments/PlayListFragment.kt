package com.example.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.data.states.PlaylistState
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.presentation.recycler.PlaylistAdapter
import com.example.playlistmaker.presentation.viewmodels.library.PlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {
    private val viewModel by viewModel<PlayListViewModel>()

    companion object {
        fun newInstance() = PlayListFragment()
    }
    private var _binding: PlaylistFragmentBinding? = null
    private val binding get() = _binding!!

    private val playlists = mutableListOf<Playlist>()
    private var adapter = PlaylistAdapter(playlists){}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.playlist.adapter = adapter
        binding.playlist.layoutManager =
            GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)
        viewModel.fill()
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekFragment_to_playlistCreatorFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        viewModel.fill()
    }
    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> showContent(state.items)
            is PlaylistState.Empty -> showEmpty()
        }
    }
    private fun showEmpty() {
        binding.messageText.visibility = View.VISIBLE
        binding.messageImage.visibility = View.VISIBLE
        binding.playlist.visibility = View.GONE
    }
    private fun showContent(items: List<Playlist>) {
        binding.messageText.visibility = View.GONE
        binding.messageImage.visibility = View.GONE
        binding.playlist.visibility = View.VISIBLE
        playlists.clear()
        playlists.addAll(items)
        adapter.notifyDataSetChanged()
    }
}