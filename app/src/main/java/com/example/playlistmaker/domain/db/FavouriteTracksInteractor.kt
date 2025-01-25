package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksInteractor {
    fun showTracks(): Flow<List<Track>>
    suspend fun updateFavorite(track: Track): Track
}