package com.example.playlistmaker.presentation.viewmodels.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.states.PlaylistState
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.PlaylistLibraryInteractor
import kotlinx.coroutines.launch

class PlayListLibraryViewModel(private val interactor: PlaylistLibraryInteractor) : ViewModel() {
    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData
    fun fill() {
        viewModelScope.launch {
            interactor.getPlayLists()
                .collect { playLists ->
                    renderState(processResult(playLists))
                }
        }
    }
    private fun processResult(albums: List<Playlist>): PlaylistState {
        return if (albums.isEmpty()) {
            PlaylistState.Empty
        } else {
            PlaylistState.Content(albums)
        }
    }
    private fun renderState(state: PlaylistState) {
        stateLiveData.postValue(state)
    }
}