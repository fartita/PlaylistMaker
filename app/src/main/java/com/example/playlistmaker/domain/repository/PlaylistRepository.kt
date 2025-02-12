package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getPlayLists(): Flow<List<Playlist>>
    suspend fun addPlaylist(playList: Playlist):Long
    suspend fun addTrack(track: Track, playList: Playlist)
    suspend fun getTrackList(playList: Playlist): List<Track>
}