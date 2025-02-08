package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun  addFavoriteTrack(track: Track)
    suspend fun deleteFavoriteTrack(track: Track)
    fun getFavoriteChecked(id: Long): Flow<Track?>
}