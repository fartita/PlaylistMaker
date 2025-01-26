package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackHistoryRepository {
    fun getTrackList(): Flow<List<Track>>
    fun setTrack(track: Track)
    fun clear()
}