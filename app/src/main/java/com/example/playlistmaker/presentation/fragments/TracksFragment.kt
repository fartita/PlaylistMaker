package com.example.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.data.states.FavouriteState
import com.example.playlistmaker.databinding.TracksFragmentBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.PlayerActivity
import com.example.playlistmaker.presentation.recycler.FavouriteTracksAdapter
import com.example.playlistmaker.presentation.viewmodels.library.TracksViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TracksFragment : Fragment() {
    private val viewModel by viewModel<TracksViewModel>()
    companion object {
        fun newInstance() = TracksFragment()
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
    }

    private val tracks = mutableListOf<Track>()
    private var adapter = FavouriteTracksAdapter(tracks) {
        if (clickDebounce()) {
            viewModel.setTrack(it)
            findNavController().navigate(R.id.action_mediatekFragment_to_audioPlayer)
        }
    }
    private var isClickAllowed = true

    private var _binding: TracksFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TracksFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvFavourite.adapter = adapter
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
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

    private fun render(state: FavouriteState) {
        when (state) {
            is FavouriteState.Content -> showContent(state.tracks)
            is FavouriteState.Empty -> showEmpty()
        }
    }
    private fun showEmpty() {
        binding.messageText.visibility = View.VISIBLE
        binding.messageImage.visibility = View.VISIBLE
        binding.rvFavourite.visibility = View.GONE
    }
    private fun showContent(trackList: List<Track>) {
        binding.messageText.visibility = View.GONE
        binding.messageImage.visibility = View.GONE
        binding.rvFavourite.visibility = View.VISIBLE
        tracks.clear()
        tracks.addAll(trackList)
        adapter.notifyDataSetChanged()
    }
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }
}