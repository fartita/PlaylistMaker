package com.example.playlistmaker.domain.playlist

import android.net.Uri
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import java.io.File

interface PlaylistCreatorInteractor {
    suspend fun createPlaylist(playListName: String, description: String, fileDir: String): Long
    fun saveImage(filePath: File, savePlaylist: String, uri: Uri)

    suspend fun updatePlayList(playList: Playlist): Flow<Playlist>
}