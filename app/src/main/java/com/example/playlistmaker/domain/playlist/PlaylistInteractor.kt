package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getPlayList(playListId: Long): Flow<Playlist>
    fun getTracks(playList: Playlist):  Flow<List<Track>>
    fun getPlayListTime(tracks: List<Track>): Int
    suspend fun delete(playlist: Playlist)
    suspend fun removeTrack(track: Track, playList: Playlist)
}