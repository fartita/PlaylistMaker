package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.data.db.model.PlaylistDb
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getPlayLists(): Flow<List<Playlist>>
    suspend fun addPlaylist(playList: Playlist):Long
    suspend fun addTrack(track: Track, playList: Playlist)
    suspend fun getTrackList(playListId: Long): List<Track>
    fun getPlayList(playListId: Long): Flow<Playlist>
    suspend fun delete(playlist: Playlist)
    suspend fun removeTrack(track: Track, playList: Playlist)
    suspend fun updatePlayList(playList: Playlist)
}