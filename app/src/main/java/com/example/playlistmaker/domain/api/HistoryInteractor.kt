package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface HistoryInteractor {
    fun getTrackList(): Flow<List<Track>>
    fun setTrack(track: Track)
    fun clear()
    fun getTrack(): Track
}