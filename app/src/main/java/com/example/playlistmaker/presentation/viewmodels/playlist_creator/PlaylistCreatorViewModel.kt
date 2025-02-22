package com.example.playlistmaker.presentation.viewmodels.playlist_creator

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.PlaylistCreatorInteractor
import java.net.URI

class PlaylistCreatorViewModel(private val interactor: PlaylistCreatorInteractor) : ViewModel() {
    private var playListName: String = ""
    private var description: String = ""
    private var fileDir: Uri? = null
    suspend fun savePlaylist(toURI: URI): String {
        val fileName = if (fileDir != null) {
            (interactor.savePlaylist(playListName, description, toURI.toString()))
        } else {
            interactor.savePlaylist(playListName, description, "")
        }
        return fileName.toString()
    }
    fun setName(changedText: String) {
        if (playListName == changedText) {
            return
        }
        this.playListName = changedText
    }
    fun setDescription(changedText: String) {
        if (description == changedText) {
            return
        }
        this.description = changedText
    }
    fun setUri(uri: Uri) {
        if (fileDir == uri) {
            return
        }
        this.fileDir = uri
    }
    fun getUri(): Uri? {
        return fileDir
    }
    fun getMessage(context: Context): String {
        val message = context.resources.getString(R.string.create_playlist)
        return message+playListName
    }
    fun checkInput(): Boolean {
        return (fileDir != null) || playListName.isNotEmpty() || description.isNotEmpty()
    }
}