package com.example.playlistmaker.domain.playlist

interface PlaylistCreatorInteractor {
    suspend fun savePlaylist(playListName: String, description: String, fileDir: String): Long
}