package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.FavouriteTracksRepository
import kotlinx.coroutines.flow.Flow

class FavouriteTracksInteractorImpl(private val favoriteTracksRepository: FavouriteTracksRepository) : FavouriteTracksInteractor {
    override fun showTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getFavoriteTracks()
    }
    override suspend fun updateFavorite(track: Track): Track {
        return if (track.isFavourite) {
            favoriteTracksRepository.deleteFavoriteTrack(track)
            track.isFavourite = false
            track
        } else {
            track.isFavourite = true
            favoriteTracksRepository.addFavoriteTrack(track)
            track
        }
    }

    override suspend fun getTrack(trackId: Long): Flow<Track?> {
        return favoriteTracksRepository.getFavoriteChecked(trackId)
    }
}