package com.example.playlistmaker.presentation.viewmodels.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.share.SharingInteractor
import kotlinx.coroutines.launch

class OnePlayListViewModel(
    val interactor: PlaylistInteractor,
    val sharingInteractor: SharingInteractor,
    val historyInteractor: HistoryInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<Playlist>()
    fun observeState(): LiveData<Playlist> = stateLiveData

    private val tracksLiveData = MutableLiveData<List<Track>>()
    fun observeTracks(): LiveData<List<Track>> = tracksLiveData

    fun getCurrentPlayList(playList: Playlist) {
        renderState(playList)
    }

    fun getUpdatePlayList() {
        viewModelScope.launch {
            stateLiveData.value?.let {
                interactor.getPlayList(it.id)
                    .collect { playList ->
                        renderState(playList)
                    }
            }
        }
    }

    fun getTracks() {
        viewModelScope.launch {
            interactor.getTracks(stateLiveData.value!!)
                .collect { tracks ->
                    renderTracks(tracks)
                }
        }
    }

    fun getTracksTime(tracks: List<Track>): Int {
        return interactor.getPlayListTime(tracks)
    }

    fun sharePlayList(playlist: Playlist) {
        sharingInteractor.sharePlayList(playlist)

    }

    fun deletePlayList(playlist: Playlist) {
        viewModelScope.launch {
            interactor.delete(playlist)
        }
    }

    fun setTrack(track: Track){
        historyInteractor.setTrack(track)
    }

    fun removeTrack(track: Track, playList: Playlist) {
        viewModelScope.launch {
            interactor.removeTrack(track, playList)
            interactor.getPlayList(playList.id).collect {
                renderState(it)
            }
        }
    }

    private fun renderState(state: Playlist) {
        stateLiveData.postValue(state)
    }

    private fun renderTracks(tracks: List<Track>) {
        tracksLiveData.postValue(tracks)
    }
}