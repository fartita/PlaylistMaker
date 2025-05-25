package com.example.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatekBinding
import com.example.playlistmaker.presentation.compose.AppTheme
import com.example.playlistmaker.presentation.compose.MediatekScreen
import com.example.playlistmaker.presentation.recycler.LibraryViewPageAdapter
import com.example.playlistmaker.presentation.viewmodels.activity.HostPlaylistViewModel
import com.example.playlistmaker.presentation.viewmodels.library.PlayListLibraryViewModel
import com.example.playlistmaker.presentation.viewmodels.library.TracksViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatekFragment: Fragment() {
//    private var _binding: FragmentMediatekBinding? = null
//    private val binding get() = _binding!!

    private val tracksViewModel by viewModel<TracksViewModel>()
    private val playlistsViewModel by viewModel<PlayListLibraryViewModel>()
    private val hostViewModel by activityViewModel<HostPlaylistViewModel>()


//    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                    MediatekScreen(
                        tracksViewModel = tracksViewModel,
                        playListsViewModel = playlistsViewModel,
                        onTrackClick = { track ->
                            tracksViewModel.setTrack(track)
                            findNavController().navigate(R.id.action_mediatekFragment_to_audioPlayer)
                        },
                        onPlaylistClick = { playList ->
                            hostViewModel.setPlayList(playList)
                            findNavController().navigate(
                                R.id.action_mediatekFragment_to_playlistFragment,
                            )
                        },
                        onNewPlaylistClick = {
                            findNavController().navigate(R.id.action_mediatekFragment_to_playlistCreatorFragment)
                        })
            }
        }
    }


//    override fun onDestroyView() {
//        tabMediator.detach()
//        super.onDestroyView()
//        _binding = null
//    }
}