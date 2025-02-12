package com.example.playlistmaker.domain.playlist

import android.content.Context
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getPlayLists(): Flow<List<Playlist>>
    suspend fun addTrack(context: Context, track: Track, playList: Playlist): String
}