package com.example.playlistmaker.presentation.fragments

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.presentation.MainActivity
import com.example.playlistmaker.presentation.viewmodels.activity.HostPlaylistViewModel
import com.example.playlistmaker.presentation.viewmodels.playlist_editor.PlaylistEditorViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistEditorFragment: PlaylistCreatorFragment() {

    private lateinit var playList: Playlist
    override val viewModel by viewModel<PlaylistEditorViewModel>()
    private val hostViewModel by activityViewModel<HostPlaylistViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createButton.text = requireActivity().resources.getString(R.string.save)
        binding.headText.text = requireActivity().resources.getString(R.string.edit)
        playList = hostViewModel.getPlayList().value!!
        viewModel.observeSaveState().observe(viewLifecycleOwner) {
            rengerSave(it)
        }

        //val file = File("${playList.imageUrl}/${playList.id}.jpg")
        val filePath =
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                MainActivity.ALBUM
            )
        val file = File(filePath, "${playList.id}.jpg")
        binding.playListImage.setScaleType(ImageView.ScaleType.CENTER_CROP)
        binding.playListImage.setBackgroundResource(0)
        showPicture(file.toUri().toString())
        binding.nameET.setText(playList.name)
        binding.descriptionET.setText(playList.description)
    }

    fun rengerSave(playList: Playlist) {
        hostViewModel.setPlayList(playList)
        findNavController().navigateUp()
    }

    override fun savePlaylist() {
        viewModel.savePlaylist(filePath, playList)
    }

    override fun goBack() {
        findNavController().navigateUp()
    }
}