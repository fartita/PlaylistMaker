package com.example.playlistmaker.presentation.fragments

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.MainActivity
import com.example.playlistmaker.presentation.recycler.TracksInPlaylistAdapter
import com.example.playlistmaker.presentation.viewmodels.activity.HostPlaylistViewModel
import com.example.playlistmaker.presentation.viewmodels.library.OnePlayListViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistFragment : Fragment() {
    private val viewModel by viewModel<OnePlayListViewModel>()
    private val hostViewModel by activityViewModel<HostPlaylistViewModel>()
    private var _binding: PlaylistFragmentBinding? = null
    private val binding get() = _binding!!
    private val tracks = mutableListOf<Track>()
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    lateinit var playlist: Playlist
    private lateinit var adapter: TracksInPlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUpdatePlayList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist = hostViewModel.getPlayList().value!!
        viewModel.getCurrentPlayList(playlist)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeTracks().observe(viewLifecycleOwner) {
            showContent(it)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.rvTrack.isVisible = true
                    }

                    else -> {
                        binding.rvTrack.isVisible = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        binding.menuButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            showMenu(playlist)

        }
        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            viewModel.setTrack(track)
            findNavController().navigate(
                R.id.action_playListFragment_to_audioPlayer
            )
        }
        binding.shareButton.setOnClickListener { share() }
        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        binding.deleteTextMenu.setOnClickListener {
            BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
            MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                .setMessage(
                    requireActivity().resources.getString(R.string.delete_playlist_alert_title)
                        .format(playlist.name)
                )
                .setNegativeButton(R.string.no_button) { _, _ ->
                }
                .setPositiveButton(R.string.yes_button) { _, _ ->
                    findNavController().navigateUp()
                    viewModel.deletePlayList(playlist)
                }.show()
        }
        binding.shareTextMenu.setOnClickListener {
            BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
            share()
        }
        binding.updateTextMenu.setOnClickListener {
            hostViewModel.setPlayList(playlist)
            findNavController().navigate(R.id.action_playListFragment_to_playlistEditorFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun share() {
        if (playlist.trackCount > 0) viewModel.sharePlayList(playlist)
        else showMistakeDialog()
    }

    private fun showMistakeDialog() {
        MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
            .setMessage(R.string.share_mistake)
            .setNeutralButton(
                R.string.ok
            ) { _, _ -> }.show()

    }

    private fun render(playlist: Playlist) {
        adapter = TracksInPlaylistAdapter(tracks,
            MaterialAlertDialogBuilder(
                requireActivity(),
                R.style.AlertDialogTheme
            ).setTitle(R.string.delete_track_alert_title)
                .setMessage(R.string.delete_track_alert_message),
            { track ->
                onTrackClickDebounce(track)
            },
            { track ->
                viewModel.removeTrack(track, playlist)
            })
        binding.rvTrack.adapter = adapter
        val filePath =
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                MainActivity.ALBUM
            )
        val file = File(filePath, "${playlist.id}.jpg")
        Glide.with(requireActivity())
            .load(file.toUri().toString())
            .placeholder(R.drawable.ic_track)
            .transform(
                CenterCrop()
            )
            .into(binding.cover)
        binding.title.text = playlist.name
        binding.description.text = playlist.description
        val timeText = viewModel.getTracksTime(playlist.tracks)
        binding.tracksTime.text = requireActivity().resources.getQuantityString(
            R.plurals.minutes,
            timeText, timeText
        )
        val countText = requireActivity().resources.getQuantityString(
            R.plurals.tracksContOfList,
            playlist.trackCount.toInt(), playlist.trackCount
        )
        binding.trackCount.text = countText
        viewModel.getTracks()
        binding.tracksBottomSheet.doOnNextLayout {
            val openMenuLocation = IntArray(2)
            binding.shareButton.getLocationOnScreen(openMenuLocation)

            val openMenuHeightFromBottom =
                binding.root.height - openMenuLocation[1] - resources.getDimensionPixelSize(R.dimen.margin_8)

            val bottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet)
            bottomSheetBehavior.peekHeight = openMenuHeightFromBottom
        }
    }

    private fun showContent(trackList: List<Track>) {
        tracks.clear()
        tracks.addAll(trackList.reversed())
        adapter.notifyDataSetChanged()
        if (trackList.isEmpty()) showMistakeDialog()

    }

    private fun showMenu(playlist: Playlist) {
        val filePath =
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                MainActivity.ALBUM
            )
        val file = File(filePath, "${playlist.id}.jpg")
        Glide.with(requireActivity())
            .load(file.toUri().toString())
            .placeholder(R.drawable.ic_track)
            .transform(
                CenterCrop()
            )
            .into(binding.item.plImage)
        binding.item.plName.text = playlist.name
        val countText = requireActivity().resources.getQuantityString(
            R.plurals.tracksContOfList,
            playlist.trackCount.toInt(), playlist.trackCount
        )
        binding.item.plCount.text = countText

        binding.deleteTextMenu.setOnClickListener {
            BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
            MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                .setMessage(
                    requireActivity().resources.getString(R.string.delete_playlist_alert_title)
                        .format(playlist.name)
                )
                .setNegativeButton(R.string.no_button) { _, _ ->
                }
                .setPositiveButton(R.string.yes_button) { _, _ ->
                    findNavController().navigateUp()
                    viewModel.deletePlayList(playlist)
                }.show()
        }

    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
    }

    private fun <T> debounce(delayMillis: Long,
                             coroutineScope: CoroutineScope,
                             useLastParam: Boolean,
                             action: (T) -> Unit): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            if (useLastParam) {
                debounceJob?.cancel()
            }
            if (debounceJob?.isCompleted != false || useLastParam) {
                debounceJob = coroutineScope.launch {
                    delay(delayMillis)
                    action(param)
                }
            }
        }
    }
}