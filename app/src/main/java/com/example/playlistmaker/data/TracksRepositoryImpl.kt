package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.SearchRequest
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.data.dto.TrackResponse

class TracksRepositoryImpl (private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): List<Track>? {
        val response = networkClient.doRequest(SearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackResponse).results.map {
                Track(it.trackName, it.artistName, it.trackTime, it.artworkUrl100, it.collectionName,it.releaseDate,it.primaryGenreName,it.country,it.previewUrl) }
        } else {
            return null
        }
    }
}