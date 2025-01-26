package com.example.playlistmaker.data

import com.example.playlistmaker.data.db.TrackDao
import com.example.playlistmaker.data.db.TrackDbConvertor
import com.example.playlistmaker.data.db.model.TrackDb
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.FavouriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(private val trackDao: TrackDao,
                                    private val trackDbConvertor: TrackDbConvertor,) :
    FavouriteTracksRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = trackDao.getFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun addFavoriteTrack(track: Track) {
        val trackDb = convertToTrackEntity(track)
        trackDao.insertTrack(trackDb)
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        val trackDb = trackDao.getFavouriteTrack(track.trackId)
        if (trackDb != null) {
            trackDao.delete(trackDb)
        }
    }

    override fun getFavoriteChecked(id: Long): Flow<Track?> = flow{
        val trackDb = trackDao.getFavouriteTrack(id)
        if(trackDb != null){
            emit(trackDbConvertor.map(trackDb) )
        }
        else{emit(null)}
    }

    private fun convertFromTrackEntity(tracks: List<TrackDb>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
    private fun convertToTrackEntity(track: Track): TrackDb {
        val date = System.currentTimeMillis()
        return trackDbConvertor.map(track, date)
    }
}