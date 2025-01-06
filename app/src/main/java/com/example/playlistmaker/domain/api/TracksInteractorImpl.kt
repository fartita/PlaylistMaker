package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<List<Track>?>{
        return repository.searchTracks(expression)
    }
}