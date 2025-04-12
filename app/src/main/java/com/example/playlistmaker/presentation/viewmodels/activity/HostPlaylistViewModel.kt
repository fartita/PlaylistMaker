package com.example.playlistmaker.presentation.viewmodels.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.model.Playlist

class HostPlaylistViewModel : ViewModel() {
    private val playListLiveData = MutableLiveData<Playlist>()
    fun setPlayList(playList: Playlist) {
        playListLiveData.postValue(playList)
    }

    fun getPlayList(): LiveData<Playlist> = playListLiveData
}