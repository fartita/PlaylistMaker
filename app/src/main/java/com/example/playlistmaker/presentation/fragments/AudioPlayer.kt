package com.example.playlistmaker.presentation.fragments

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.states.PlayerState
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.recycler.PlaylistPlayerAdapter
import com.example.playlistmaker.presentation.services.PlayerService
import com.example.playlistmaker.presentation.util.ConnectionBroadcastReceiver
import com.example.playlistmaker.presentation.viewmodels.player.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.security.Permission
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayer: Fragment() {


    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!


    private val playlists = mutableListOf<Playlist>()
    lateinit var track: Track
    private val adapter = PlaylistPlayerAdapter(playlists) {
        viewModel.addToPlaylist(requireContext(), track, it)
    }

    private val viewModel by viewModel<PlayerViewModel>()
    private val connectionBroadcastReceiver = ConnectionBroadcastReceiver()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.MusicServiceBinder
            viewModel.setAudioPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bindMusicService(track)
        } else {
            Toast.makeText(requireContext(), "Can't bind service!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        track = viewModel.getTrack()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            else{
                bindMusicService(track)
            }
        }
        else{
            bindMusicService(track)
        }
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }



        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                        viewModel.renderPlayLists()
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        binding.recyclerView.adapter = adapter
        binding.playButton.onTouchListener = { viewModel.onPlayerButtonClicked() }

        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }


        viewModel.observeFavouriteState().observe(viewLifecycleOwner){
            favouriteRender(it)
        }

        viewModel.observePlaylistState().observe(viewLifecycleOwner) {
            renderPlayList(it)
        }
        viewModel.observeAddDtate().observe(viewLifecycleOwner) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            showToast(it)
        }
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        binding.likeButton.setOnClickListener { viewModel.onFavouriteClicked(track) }
        binding.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.addPlaylistButton.setOnClickListener {
            findNavController().navigate((R.id.action_audioPlayer_to_playlistCreatorFragment))
        }
        binding.title.text = track.trackName
        binding.artist.text = track.artistName
        binding.albumName.text = track.collectionName
        binding.year.text = track.releaseDate.substring(0, 4)
        binding.styleName.text = track.primaryGenreName
        binding.countryName.text = track.country
        binding.trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)

        Glide.with(requireActivity())
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.ic_track)
            .centerCrop()
            .transform(RoundedCorners(requireActivity().resources.getDimensionPixelSize(R.dimen.margin_8)))
            .into(binding.cover)


        viewModel.prepare(track)
    }


    private fun favouriteRender(favouriteChecked: Boolean) {
        if (favouriteChecked)
            binding.likeButton.setImageResource(R.drawable.like_button_on)
        else binding.likeButton.setImageResource(R.drawable.like_button_off)
    }


    private fun progressTimeViewUpdate(progressTime: String) {
        binding.progressTime.text = progressTime
    }


    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(connectionBroadcastReceiver)
        viewModel.showNotification()
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            requireContext(),
            connectionBroadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        viewModel.hideNotification()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindMusicService()
    }

    private fun bindMusicService(track: Track) {
        val intent = Intent(requireContext(), PlayerService::class.java).apply {
            putExtra("song_url", track.previewUrl)
            putExtra("song_title", track.trackName)
            putExtra("song_artist",track.artistName)
        }
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindMusicService() {
        requireContext().unbindService(serviceConnection)
    }


    private fun renderPlayList(list: List<Playlist>) {
        playlists.clear()
        playlists.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun render(state: PlayerState) {
        binding.playButton.changeButtonStatus(state.buttonState)
        progressTimeViewUpdate(state.progress)
    }


}
