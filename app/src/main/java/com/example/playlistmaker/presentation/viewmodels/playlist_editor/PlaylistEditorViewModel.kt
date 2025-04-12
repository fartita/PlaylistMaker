package com.example.playlistmaker.presentation.viewmodels.playlist_editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.PlaylistCreatorInteractor
import com.example.playlistmaker.presentation.viewmodels.playlist_creator.PlaylistCreatorViewModel
import kotlinx.coroutines.launch
import java.io.File

class PlaylistEditorViewModel(val interactor: PlaylistCreatorInteractor) : PlaylistCreatorViewModel(
    interactor
) {

    private val saveLiveData = MutableLiveData<Playlist>()
    fun observeSaveState(): LiveData<Playlist> = saveLiveData

    fun savePlaylist(filePath: File, playList: Playlist) {
        viewModelScope.launch {
            if (fileDir != null) {
                saveImage(filePath, playList.id.toString())
            }
            playList.name = nameLiveData.value!!
            playList.description = descriptionLiveData.value ?: ""
            interactor.updatePlayList(playList).collect { playList ->
                renderState(playList)
            }
        }
    }

    fun renderState(playList: Playlist) {
        saveLiveData.postValue(playList)
    }
}