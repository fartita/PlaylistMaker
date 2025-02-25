package com.example.playlistmaker.domain.playlist

import android.net.Uri
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.repository.FileRepository
import com.example.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import java.io.File

class PlaylistCreatorInteractorImpl(private val repository: PlaylistRepository,
                                    private val fileRepository: FileRepository): PlaylistCreatorInteractor {
    override suspend fun createPlaylist(
        playListName: String,
        description: String,
        fileDir: String
    ): Long {
        val playlist = Playlist(0,playListName, description, fileDir, 0, mutableListOf())
        return repository.addPlaylist(playlist)
    }

    override fun saveImage(filePath: File, savePlaylist: String, uri: Uri) {
        fileRepository.saveImage(filePath, savePlaylist, uri)
    }

    override suspend fun updatePlayList(playList: Playlist): Flow<Playlist> {
        repository.updatePlayList(playList)
        return repository.getPlayList(playList.id)
    }

}