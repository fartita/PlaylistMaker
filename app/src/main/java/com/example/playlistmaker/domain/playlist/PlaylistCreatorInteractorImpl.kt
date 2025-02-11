package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.repository.PlaylistRepository

class PlaylistCreatorInteractorImpl(private val repository: PlaylistRepository): PlaylistCreatorInteractor {
    override suspend fun savePlaylist(
        playListName: String,
        description: String,
        fileDir: String
    ): Long {
        val playlist = Playlist(0,playListName, description, fileDir, 0, null)
        return repository.addPlaylist(playlist)
    }

}