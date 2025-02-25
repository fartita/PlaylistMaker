package com.example.playlistmaker.presentation.viewmodels.playlist_creator

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.PlaylistCreatorInteractor
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI

open class PlaylistCreatorViewModel(private val interactor: PlaylistCreatorInteractor) : ViewModel() {
    val nameLiveData = MutableLiveData<String>()
    val descriptionLiveData = MutableLiveData<String>()
    var fileDir: Uri? = null
    private val stateLiveData = MutableLiveData<String>()
    fun observeStateLiveData(): LiveData<String> = stateLiveData

    private suspend fun createPlaylist(): String {
        val fileName = if (fileDir != null) {
            (interactor.createPlaylist(nameLiveData.value!!, descriptionLiveData.value?: "", fileDir.toString()))
        } else {
            interactor.createPlaylist(nameLiveData.value!!, descriptionLiveData.value?: "", "")
        }
        return fileName.toString()
    }

    fun setName(changedText: String) {
        nameLiveData.postValue(changedText)
    }

    fun setDescription(changedText: String) {
        descriptionLiveData.postValue(changedText)
    }

    fun setUri(uri: Uri) {
        if (fileDir == uri) {
            return
        }
        this.fileDir = uri
    }

    fun checkInput(): Boolean {
        return (fileDir != null) || !nameLiveData.value.isNullOrEmpty()|| !nameLiveData.value.isNullOrEmpty()
    }

    fun saveImage(filePath: File, savePlaylist: String) {
        viewModelScope.launch {
            interactor.saveImage(filePath, savePlaylist, fileDir!!)
        }
    }

    open fun savePlaylist(filePath: File) {
        viewModelScope.launch {
            if (fileDir != null)
                saveImage(
                    filePath,
                    createPlaylist(),
                ) else createPlaylist()
            renderState(nameLiveData.value!!)
        }
    }
    private fun renderState(state: String) {
        stateLiveData.postValue(state)
    }
}