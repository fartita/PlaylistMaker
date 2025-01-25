package com.example.playlistmaker.data

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.TrackDbConvertor
import com.example.playlistmaker.data.dto.SearchRequest
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.data.dto.TrackResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl (private val networkClient: NetworkClient, private val appDatabase: AppDatabase,) : TracksRepository {
    override fun searchTracks(expression: String): Flow<List<Track>?> = flow {
        val response = networkClient.doRequest(SearchRequest(expression))
        when(response.resultCode){
            200 -> {
                with(response as TrackResponse){
                    val favoriteTracks = appDatabase.trackDao().getTrackId()
                    val data = results.map { Track(it.trackId, it.trackName, it.artistName, it.trackTime, it.artworkUrl100, it.collectionName,it.releaseDate,it.primaryGenreName,it.country,it.previewUrl, favoriteTracks.contains(it.trackId)) }
                    emit(data)
                }
            }
            else ->{
                emit(null)
            }
        }
    }
}