package com.example.playlistmaker.data

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.TrackDbConvertor
import com.example.playlistmaker.data.db.model.TrackDb
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.FavouriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(private val appDatabase: AppDatabase,
                                    private val trackDbConvertor: TrackDbConvertor,) :
    FavouriteTracksRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun addFavoriteTrack(track: Track) {
        val trackDb = convertToTrackEntity(track)
        appDatabase.trackDao().insertTrack(trackDb)
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        val trackDb = appDatabase.trackDao().getFavouriteTrack(track.trackId)
        appDatabase.trackDao().delete(trackDb)
    }

    override fun getFavoriteChecked(id: Long): Flow<List<Long>> = flow{
        val tracksId = appDatabase.trackDao().getTrackId()
        emit(tracksId)
    }

    private fun convertFromTrackEntity(tracks: List<TrackDb>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
    private fun convertToTrackEntity(track: Track): TrackDb {
        val date = System.currentTimeMillis()
        return trackDbConvertor.map(track, date)
    }
}